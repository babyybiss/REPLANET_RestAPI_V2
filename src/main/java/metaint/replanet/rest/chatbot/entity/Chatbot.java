package metaint.replanet.rest.chatbot.entity;

import javax.persistence.*;

@Entity(name = "tblChatBot")
@Table(name = "tbl_chat_bot")
public class Chatbot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_code")
    private int questionCode;
    @Column(name = "question_content")
    private String question_content;
    @Column(name = "answer_content")
    private String answer_content;

    protected Chatbot() {}

    public Chatbot(int questionCode, String question_content, String answer_content) {
        this.questionCode = questionCode;
        this.question_content = question_content;
        this.answer_content = answer_content;
    }

    public int getQuestionCode() {
        return questionCode;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    @Override
    public String toString() {
        return "Chatbot{" +
                "questionCode=" + questionCode +
                ", question_content='" + question_content + '\'' +
                ", answer_content='" + answer_content + '\'' +
                '}';
    }
}
