package org.example.activityservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityRequestDto {

    private Long activityId;

    private String activityName;

    private Long categoryId;

    private String categoryName;

    private String address;

    private String coordinates;

    private LocalDateTime joinDeadline;

    private LocalDateTime activityStart;

    private String status;

    private String comment;

    private Integer participantsRequired;

    private Integer participantsJoined;

    private Long creatorId;

    private String creatorNickname;

    private List<Long> participants;
}
