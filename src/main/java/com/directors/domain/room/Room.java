package com.directors.domain.room;

import com.directors.domain.chat.Chat;
import com.directors.domain.common.BaseEntity;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 04.28 Question JPA 적용 시 수정
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "question_id")
    // private Question question;
    private Long questionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id")
    private User director;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questioner_id")
    private User questioner;

    // TODO: 04.28 보통 페이징 방식으로 조회하므로, 유지 여부 생각해보기
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Chat> chatList = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    // TODO: 04.28 Question JPA 적용 시 수정
    public static Room of(Long questionId, User director, User questioner) {
        return Room.builder()
                .questionId(questionId)
                .director(director)
                .questioner(questioner)
                .build();
    }

    public void validateRoomUser(String sendUserId) {
        if (!(sendUserId.equals(this.director.getId()) || sendUserId.equals(this.questioner.getId()))) {
            throw new RoomNotFoundException(this.id, sendUserId);
        }
    }
}
