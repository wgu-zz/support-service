class QuestionsController < ApplicationController

  before_filter :init

  def index
    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    @questions = @so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page)["items"]

    render_questions
  end

  def my
    @assigned_questions = current_user.nil? ? {} : current_user.assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    # so list of users questions
    @questions = @assigned_questions.length > 0 ? @so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page, :ids => users_question_ids)["items"] : []

    render_questions

  end

  def all_open
    @assigned_questions = current_user.nil? ? {} : current_user.open_assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.open_assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    # so list of users questions
    @questions = @all_assigned_questions.length > 0 ? @so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page, :ids => all_question_ids)["items"] : []
    render_questions
  end

  def my_open
    @assigned_questions = current_user.nil? ? {} : current_user.open_assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    # so list of users questions
    @questions = @assigned_questions.length > 0 ? @so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page, :ids => users_question_ids)["items"] : []
    render_questions
  end

  def unassigned
    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    @questions = @so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page)["items"]

    @questions.reject! { |q| @all_assigned_questions.has_key? q["question_id"].to_s }
    render_questions
  end

  def unanswered
    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    @questions = @so_client.questions_unanswered?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => @page)["items"]

    render_questions
  end

  def search
    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    @questions = @so_client.search?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :intitle => params[:q], :pagesize => 20, :page => @page)["items"]

    render_questions
  end

  def action_missing(meth, *args, &block)
    if meth.to_s =~ /^questions_for_(.+)$/
      puts "getting #{$1}"  
      @questions = tagged_questions_for_span($1.to_sym)

      respond_to do |format|
        format.html { render :text => @questions.count }
        format.json { render :json => @questions }
      end
    else
      super
    end
  end

  def tagged_questions_for_span(span_type) # :day, :week, :month
    start_of_day = Time.now.beginning_of_day
    epoch = span_type == :day ? start_of_day.to_i : (start_of_day - (1.send(span_type))).to_i

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :fromdate => epoch, :pagesize => 100)["items"]
  end

  def show
    
    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]
    @question = so_client.instance_eval("questions_#{params[:id]}? :filter => 'withbody'")["items"].first
    @comments = so_client.instance_eval("questions_#{params[:id]}_comments? :filter => 'withbody'")["items"]

    # find assignment
    @assignment = Assignment.find_by_question_id(params[:id]) || Assignment.new
    note = @assignment.notes.build

    respond_to do |format|
      format.html
      format.json { render :json => @question }
    end

  end

  private

  def init
    @page = params[:page] || 1
    @so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]
  end

  def render_questions
    respond_to do |format|
      format.html { 
        if @page == 1
          render 'question_results'
        else
          render :partial => 'questions', :locals => { :questions => @questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
        end
      }
      format.json { render :json => @questions }
    end
  end
end
