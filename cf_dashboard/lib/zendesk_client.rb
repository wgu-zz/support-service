require 'httparty'
require 'json'

class ZendeskClient
  include HTTParty

  ENDPOINT="http://support.cloudfoundry.com/"

  def self.method_missing(meth, *args, &block)
    
    path = meth.to_s.gsub(/\_/, '/')
    
    if not args.nil?
      options[:query].merge! args 
      path = path.gsub(/\/ids/, "/#{args[:ids]}") if args.has_key? :ids
    end

    http_method = meth.to_s =~ /^.+\?$/ ? "GET" : "POST"
    path = "#{ENDPOINT}/#{path.match(/.+[^?!]/).to_s}.json"

    if http_method == "POST"
      @response = self.class.post(path, options).body
    else
      @response = self.class.get(path, options).body
    end

    JSON.parse @response
  end

end