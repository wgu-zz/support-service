module QuestionsHelper
  def status_class_for_question(question)
    return "complete" if question["is_answered"] == true
    return "has-answers" if question["is_answered"] == false and question["answer_count"] > 0
    return "unanswered" if question["is_answered"] == false and question["answer_count"] == 0
  end
end
