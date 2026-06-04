package com.orientation.backend.sessions.domain.model.entities;

import com.orientation.backend.sessions.domain.model.enums.SessionOrigin;
import com.orientation.backend.sessions.domain.model.enums.SessionType;
import com.orientation.backend.sessions.domain.model.exceptions.InvalidCancellationReasonException;
import com.orientation.backend.sessions.domain.model.exceptions.SessionAlreadyInactiveException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Session {

    private UUID id;
    private String title;
    private String description;
    private String motivation;
    private SessionType sessionType;
    private SessionOrigin sessionOrigin;
    private boolean allWelcome;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime cancelledAt;
    private String cancelledReason;
    private LocalDateTime infoSentAt;
    private Double score;
    private String summary;


    //CONSTRUCTOR (Private - Use Builder)
    private Session(Builder builder) {
        this.id = builder.id;
        this.title = Objects.requireNonNull(builder.title);
        this.description = builder.description;
        this.motivation = builder.motivation;
        this.sessionType = Objects.requireNonNull(builder.sessionType);
        this.sessionOrigin = Objects.requireNonNull(builder.sessionOrigin);
        this.allWelcome = builder.allWelcome;
        this.startDateTime = builder.startDateTime;
        this.endDateTime = builder.endDateTime;
        this.cancelledAt = builder.cancelledAt;
        this.cancelledReason = builder.cancelledReason;
        this.infoSentAt = builder.infoSentAt;
        this.score = builder.score;
        this.summary = builder.summary;
    }

    // ============================================
    // FACTORY METHOD (Builder)
    // ============================================
    public static Builder builder(){
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getMotivation() {
        return motivation;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public SessionOrigin getSessionOrigin() {
        return sessionOrigin;
    }

    public boolean isAllWelcome() {
        return allWelcome;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public LocalDateTime getInfoSentAt() {
        return infoSentAt;
    }

    public Double getScore() {
        return score;
    }

    public String getSummary() {
        return summary;
    }


    // ============================================
    // BUSINESS METHODS - Cancel Session Method
    // ============================================

    public void cancel(String cancelledReason){
        if(this.cancelledAt != null){
            throw new SessionAlreadyInactiveException();
        }

        if(cancelledReason == null || cancelledReason.isBlank()){
            throw new InvalidCancellationReasonException();
        }

        this.cancelledAt = LocalDateTime.now();
        this.cancelledReason = cancelledReason;
    }

    // ============================================
    // BUILDER (Inner Static Class)
    // ============================================

    public static class Builder{
        private UUID id;
        private String title;
        private String description;
        private String motivation;
        private SessionType sessionType;
        private SessionOrigin sessionOrigin;
        private boolean allWelcome;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private LocalDateTime cancelledAt;
        private String cancelledReason;
        private LocalDateTime infoSentAt;
        private Double score;
        private String summary;

        private Builder(){

        }

        public Builder id(UUID id){
            this.id = id;
            return this;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder description(String description){
            this.description = description;
            return this;
        }

        public Builder motivation(String motivation){
            this.motivation = motivation;
            return this;
        }

        public Builder sessionType(SessionType sessionType){
            this.sessionType = sessionType;
            return this;
        }

        public Builder sessionOrigin(SessionOrigin sessionOrigin){
            this.sessionOrigin = sessionOrigin;
            return this;
        }

        public Builder allWelcome(boolean allWelcome){
            this.allWelcome = allWelcome;
            return this;
        }

        public Builder startDateTime(LocalDateTime startDateTime){
            this.startDateTime = startDateTime;
            return this;
        }

        public Builder endDateTime(LocalDateTime endDateTime){
            this.endDateTime = endDateTime;
            return this;
        }

        public Builder cancelledAt(LocalDateTime cancelledAt){
            this.cancelledAt = cancelledAt;
            return this;
        }

        public Builder cancelledReason(String cancelledReason){
            this.cancelledReason = cancelledReason;
            return this;
        }

        public Builder infoSentAt(LocalDateTime infoSentAt){
            this.infoSentAt = infoSentAt;
            return this;
        }

        public Builder score(double score){
            this.score = score;
            return this;
        }

        public Builder summary(String summary){
            this.summary = summary;
            return this;
        }

        /**
         * Builds the Session instance.
         * Validates all required fields are present.
         *
         * @return new Session instance
         * @throws NullPointerException if required fields are null
         * @throws IllegalArgumentException if validation fails
         */
        public Session build(){
            return new Session(this);
        }
    }
}
