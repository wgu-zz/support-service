class User < ActiveRecord::Base
  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable,
  # :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :registerable, :omniauthable,
         :recoverable, :rememberable, :trackable, :validatable

  # Setup accessible (or protected) attributes for your model
  attr_accessible :email, :password, :password_confirmation, :remember_me, :display_name, :profile_image
  # attr_accessible :title, :body

  has_many :assignments

  def self.find_for_stack_overflow_oauth(access_token, signed_in_resource=nil)
    
    data = access_token.info

    puts "#" * 10000
    puts data.inspect

    if user = User.where(:email => data.email).first
      user
    else # Create a user with a stub password. 
      User.create!(:email => data.email, :password => Devise.friendly_token[0,20]) 
    end

  end

  def open_assigned_questions
    table = {}
    assignments.where("status != 'Closed'").each { |x| table[x.question_id.to_s] = x }

    table
  end

  def assigned_questions
    table = {}
    
    assignments.all.each { |x| table[x.question_id.to_s] = x }

    table
  end


end
