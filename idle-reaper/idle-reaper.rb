require "rubygems"
require "bundler/setup"
require "date"
require "optparse"
require "digest/sha1"
require "net/smtp"
require "csv"
require "json"
require "vmc"

class IdleReaper
  TARGET_FILE = "~/.vmc_target"
  TOKENS_FILE = "~/.vmc_token"

  def initialize
    @options = {}
  end

  def run!
    parse_options!
    reap!
  end

  def parse_options!
    OptionParser.new do |o|
      o.banner = "usage: ruby idle-reaper.rb [INPUT] [options]"

      o.separator ""
      o.separator "Input source must be in CSV format:"
      o.separator ""
      o.separator "  appname,user@foo.com"
      o.separator "  appname,user2@bar.com"
      o.separator ""
      o.separator "Additional values will be ignored."
      o.separator ""
      o.separator ""
      o.separator "Options:"

      o.on("-i INPUT", "--input INPUT",
          "Read users from INPUT (defaults to stdin)") do |v|
        @options[:input] = File.open(v, "rb")
      end


      o.on("-t TARGET", "--target TARGET", "Target URL") do |v|
        @options[:target] = v
      end

      o.on("-d", "--dry-run") do |v|
        @options[:dry_run] = v
      end

      o.on_tail("-h", "--help", "Show this message") do
        puts o
        exit
      end
    end.parse!

    @options[:input] ||= ARGV[0] || $stdin

    @options[:target] ||=
      File.open(File.expand_path(TARGET_FILE), "rb", &:read).chomp

    tokens =
      File.open(File.expand_path(TOKENS_FILE), "rb", &:read)

    @options[:token] = JSON.parse(tokens)[@options[:target]]
  end

  def dry_run?
    @options[:dry_run]
  end

  def reap!
    if dry_run?
      puts "Dry run; the following would be performed:"
    end

    client = VMC::Client.new(@options[:target], @options[:token])
    each_user(@options[:input]) do |app, user|

      client.proxy = user

      unless dry_run?
        info = client.app_info(app)
        before = info[:state]
      end

      if before == "STOPPED"
        puts "Already stopped; skipping..."
        next
      end

      info[:state] = "STOPPED" unless dry_run?

      puts "Stopping #{user}'s #{app}..."
      client.update_app(app, info) unless dry_run?
    end
  end

  private

  def each_user(from, &blk)
    input =
      case from
      when IO
        from.read
      else
        File.open(from, "rb", &:read)
      end

    CSV.parse(input.gsub(/\r\n?/, "\n"), &blk)
  end
end

IdleReaper.new.run!
