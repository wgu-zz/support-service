class QuestionsController < ApplicationController

  def index
    page = params[:page] || 1
    
    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    # so list of users questions
    # @questions = so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation')["items"]

    # so list of all assignments
    @questions = so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => page)["items"]

    respond_to do |format|
      format.html { 
        if page == 1
          render 'question_results'
        else
          render :partial => 'questions', :locals => { :questions => @questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
        end
      }
      format.json { render :json => @questions }
    end

  end

  def my

    @assigned_questions = current_user.nil? ? {} : current_user.assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    # so list of users questions
    @questions = @assigned_questions.length > 0 ? so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => 1, :ids => users_question_ids)["items"] : []

    respond_to do |format|
      format.html { render 'question_results' }
      format.json { render :json => @questions }
    end

  end

  def all_open
    @assigned_questions = current_user.nil? ? {} : current_user.open_assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.open_assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    # so list of users questions
    @questions = @all_assigned_questions.length > 0 ? so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => 1, :ids => all_question_ids)["items"] : []

    respond_to do |format|
      format.html { render 'question_results' }
      format.json { render :json => @questions }
    end
  end

  def my_open
    @assigned_questions = current_user.nil? ? {} : current_user.open_assigned_questions
    users_question_ids = @assigned_questions.keys.join ";"

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    # so list of users questions
    @questions = @assigned_questions.length > 0 ? so_client.questions_ids?(:filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => 1, :ids => users_question_ids)["items"] : []

    respond_to do |format|
      format.html { render 'question_results' }
      format.json { render :json => @questions }
    end

  end

  def unassigned
    page = params[:page] || 1

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    @questions = so_client.questions?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => page)["items"]

    @questions.reject! { |q| @all_assigned_questions.has_key? q["question_id"].to_s }

    respond_to do |format|
      format.html { 
        if page == 1
          render 'question_results'
        else
          render :partial => 'questions', :locals => { :questions => @questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
        end
      }
      format.json { render :json => @questions }
    end

  end

  def unanswered
    page = params[:page] || 1

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    @questions = so_client.questions_unanswered?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => page)["items"]

    respond_to do |format|
      format.html { 
        if page == 1
          render 'question_results'
        else
          render :partial => 'questions', :locals => { :questions => @questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
        end
      }
      format.json { render :json => @questions }
    end
  end

  def search
    page = params[:page] || 1

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    @questions = so_client.search?(:tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :intitle => params[:q], :pagesize => 20, :page => page)["items"]

    respond_to do |format|
      format.html { 
        if page == 1
          render 'question_results'
        else
          render :partial => 'questions', :locals => { :questions => @questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
        end
      }
      format.json { render :json => @questions }
    end
  end


  def more

    page = params[:page] || 1

    @all_assigned_questions = Assignment.assignment_table
    all_question_ids = @all_assigned_questions.keys.join ";"

    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]

    @questions = @all_assigned_questions.length > 0 ? so_client.instance_eval("questions_ids? :filter => 'withbody', :order => 'desc', :sort => 'creation', :ids=>'#{all_question_ids}'")["items"] : []
    @all_questions = so_client.instance_eval("questions? :tagged => 'cloudfoundry', :filter => 'withbody', :order => 'desc', :sort => 'creation', :pagesize => 20, :page => #{page}")["items"]

    render :partial => 'questions', :locals => { :questions => @all_questions, :assigned_questions => @all_assigned_questions, :do_load_lock => true }
    
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

    query = "questions? :tagged => 'cloudfoundry', :filter => 'withbody', :fromdate => #{epoch}"
    puts query
    so_client.instance_eval(query)["items"]
  end

  def show
    
    so_client = StackOverflowClient.new_with_oauth session["oauth_creds"]
    @question = so_client.instance_eval("questions_#{params[:id]}? :filter => 'withbody'")["items"].first
 
    # find assignment
    @assignment = Assignment.find_by_question_id(params[:id]) || Assignment.new
    note = @assignment.notes.build

    respond_to do |format|
      format.html
      format.json { render :json => @question }
    end

  end

end
