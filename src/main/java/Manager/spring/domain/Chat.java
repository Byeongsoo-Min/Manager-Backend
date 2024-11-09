package Manager.spring.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GptChat> gptMessages = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChat> userMessages = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<GptChat> getGptMessages() {
        return gptMessages;
    }

    public void setGptMessages(List<GptChat> gptMessages) {
        this.gptMessages = gptMessages;
    }

    public List<UserChat> getUserMessages() {
        return userMessages;
    }

    public void setUserMessages(List<UserChat> userMessages) {
        this.userMessages = userMessages;
    }

    public void addGptMessage(GptChat message) {
        gptMessages.add(message);
        message.setChat(this);
    }

    public void removeGptMessage(GptChat message) {
        gptMessages.remove(message);
        message.setChat(null);
    }

    public void addUserMessage(UserChat message) {
        userMessages.add(message);
        message.setChat(this);
    }

    public void removeUserMessage(UserChat message) {
        userMessages.remove(message);
        message.setChat(null);
    }
}
