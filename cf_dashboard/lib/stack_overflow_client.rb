require 'httparty'
require 'json'

class StackOverflowClient
  include HTTParty
  
  def self.creds 
    return { :client_id => '426', :key => 'kJKckIcD6ALMyMqma4gbVw((', :secret => 'IaWzHqSa1lmss6JENScxwA((' } if Rails.env == "production" # live
    { :client_id => '405', :key => 'n0DtSETZdAnmqDLtIGp7og((', :secret => 'kgsOA3)dZHpmC4E0*)R72Q((' } # dev
  end

  def self.new_with_oauth(creds=nil)
    token = creds.nil? ? nil : creds.token
    self.new token
  end

  def initialize(token=nil)
    @token = token
  end

  def method_missing(meth, *args, &block)
    
    options = { :query => { :site => 'stackoverflow', :client_id => self.class.creds[:client_id], :key => self.class.creds[:key], :access_token => @token } }
    args = args.last

    path = meth.to_s.gsub(/\_/, '/')
    
    if not args.nil?
      options[:query].merge! args 
      path = path.gsub(/\/ids/, "/#{args[:ids]}") if args.has_key? :ids
    end

    http_method = meth.to_s =~ /^.+\?$/ ? "GET" : "POST"
    path = "https://api.stackexchange.com/2.0/#{path.match(/.+[^?!]/).to_s}"

    puts "### Query is ..."
    puts options[:query].inspect

    if http_method == "POST"
      @response = self.class.post(path, options).body
    else
      @response = self.class.get(path, options).body
    end

    JSON.parse @response
  end

end