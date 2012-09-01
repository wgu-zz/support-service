require 'spec_helper'

describe Incident do

  before do
    @user = User.create(email: "user@example.com")
    @incident = Incident.new(title: "Java apps unable to start")
    @incident.creator = @user
    @incident.updater = @user
  end

  subject { @incident }

  it { should respond_to(:title) }
  it { should respond_to(:creator_user_id) }
  it { should respond_to(:updater_user_id) }
  it { should respond_to(:resolved?) }
  it { should respond_to(:upcoming?) }

  it { should be_valid }

  describe "when title is not present" do
    before { @incident.title = " " }
    it { should_not be_valid }
  end

  describe "when creator is not present" do
    before { @incident.creator = nil }
    it { should_not be_valid }
  end

  describe "when updater is not present" do
    before { @incident.updater = nil }
    it { should_not be_valid }
  end

end