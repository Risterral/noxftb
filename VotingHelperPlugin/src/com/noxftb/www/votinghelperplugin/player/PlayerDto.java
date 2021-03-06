package com.noxftb.www.votinghelperplugin.player;


import java.util.Date;

public class PlayerDto {
    private Integer numberOfVotes;
    private Date lastVotingDate;

    public Integer getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(Integer numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    public Date getLastVotingDate() {
        return lastVotingDate;
    }

    public void setLastVotingDate(Date lastVotingDate) {
        this.lastVotingDate = lastVotingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerDto playerDto = (PlayerDto) o;

        if (lastVotingDate != null ? !lastVotingDate.equals(playerDto.lastVotingDate) : playerDto.lastVotingDate != null)
            return false;
        if (numberOfVotes != null ? !numberOfVotes.equals(playerDto.numberOfVotes) : playerDto.numberOfVotes != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numberOfVotes != null ? numberOfVotes.hashCode() : 0;
        result = 31 * result + (lastVotingDate != null ? lastVotingDate.hashCode() : 0);
        return result;
    }
}