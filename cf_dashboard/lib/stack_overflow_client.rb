require 'httparty'
require 'json'

class StackOverflowClient
  include HTTParty
  
  def self.creds 
    { :client_id => '405', :key => 'n0DtSETZdAnmqDLtIGp7og((', :secret => 'kgsOA3)dZHpmC4E0*)R72Q((' }
  end

  def initialize(token=nil)
    @token = token
  end

  def method_missing(meth, *args, &block)
    
    options = { :query => { :site => 'stackoverflow', :client_id => self.class.creds[:client_id], :key => self.class.creds[:key], :access_token => @token } }
    args = args.last
    options[:query].merge! args if args.length > 0

    path = meth.to_s.gsub(/\_/, '/')
    path = path.gsub(/\/ids/, "/#{args[:ids]}") if args.has_key? :ids

    http_method = meth.to_s =~ /^.+\?$/ ? "GET" : "POST"
    path = "https://api.stackexchange.com/2.0/#{path.match(/.+[^?!]/).to_s}"

    if http_method == "POST"
      @response = self.class.post(path, options).body
    else
      @response = self.class.get(path, options).body
    end

    JSON.parse @response
  end

end