package com.onpost.domain.entity.comment;

import com.onpost.domain.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Entity
@AllArgsConstructor
@DiscriminatorValue(value = "Sub")
public class SubComment extends Comment {

    @Builder
    public SubComment(String context, Member writer) {
        super(writer, context);
    }
}
