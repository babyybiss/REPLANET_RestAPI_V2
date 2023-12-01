package metaint.replanet.rest.chart.dto;

public class CountAndSumByMonthlyDTO {

    private int fullYear;
    private int monthly;
    private int allCampaigns;
    private int childCampaigns;
    private int olderCampaigns;
    private int etcCampaigns;
    private int animalCampaigns;
    private int natureCampaigns;
    private long sumCurrentBudget;
    private long sumGoalBudget;
    private long displaySumCurrentBudget;
    private long sumExpectBudget;
    private double progress;

    public CountAndSumByMonthlyDTO() {
    }

    public CountAndSumByMonthlyDTO(int fullYear, int monthly, int allCampaigns, int childCampaigns, int olderCampaigns, int etcCampaigns, int animalCampaigns, int natureCampaigns, long sumCurrentBudget, long sumGoalBudget, long displaySumCurrentBudget, long sumExpectBudget, double progress) {
        this.fullYear = fullYear;
        this.monthly = monthly;
        this.allCampaigns = allCampaigns;
        this.childCampaigns = childCampaigns;
        this.olderCampaigns = olderCampaigns;
        this.etcCampaigns = etcCampaigns;
        this.animalCampaigns = animalCampaigns;
        this.natureCampaigns = natureCampaigns;
        this.sumCurrentBudget = sumCurrentBudget;
        this.sumGoalBudget = sumGoalBudget;
        this.displaySumCurrentBudget = displaySumCurrentBudget;
        this.sumExpectBudget = sumExpectBudget;
        this.progress = progress;
    }

    public int getFullYear() {
        return fullYear;
    }

    public void setFullYear(int fullYear) {
        this.fullYear = fullYear;
    }

    public int getMonthly() {
        return monthly;
    }

    public void setMonthly(int monthly) {
        this.monthly = monthly;
    }

    public int getAllCampaigns() {
        return allCampaigns;
    }

    public void setAllCampaigns(int allCampaigns) {
        this.allCampaigns = allCampaigns;
    }

    public int getChildCampaigns() {
        return childCampaigns;
    }

    public void setChildCampaigns(int childCampaigns) {
        this.childCampaigns = childCampaigns;
    }

    public int getOlderCampaigns() {
        return olderCampaigns;
    }

    public void setOlderCampaigns(int olderCampaigns) {
        this.olderCampaigns = olderCampaigns;
    }

    public int getEtcCampaigns() {
        return etcCampaigns;
    }

    public void setEtcCampaigns(int etcCampaigns) {
        this.etcCampaigns = etcCampaigns;
    }

    public int getAnimalCampaigns() {
        return animalCampaigns;
    }

    public void setAnimalCampaigns(int animalCampaigns) {
        this.animalCampaigns = animalCampaigns;
    }

    public int getNatureCampaigns() {
        return natureCampaigns;
    }

    public void setNatureCampaigns(int natureCampaigns) {
        this.natureCampaigns = natureCampaigns;
    }

    public long getSumCurrentBudget() {
        return sumCurrentBudget;
    }

    public void setSumCurrentBudget(long sumCurrentBudget) {
        this.sumCurrentBudget = sumCurrentBudget;
    }

    public long getSumGoalBudget() {
        return sumGoalBudget;
    }

    public void setSumGoalBudget(long sumGoalBudget) {
        this.sumGoalBudget = sumGoalBudget;
    }

    public long getDisplaySumCurrentBudget() {
        return displaySumCurrentBudget;
    }

    public void setDisplaySumCurrentBudget(long displaySumCurrentBudget) {
        this.displaySumCurrentBudget = displaySumCurrentBudget;
    }

    public long getSumExpectBudget() {
        return sumExpectBudget;
    }

    public void setSumExpectBudget(long sumExpectBudget) {
        this.sumExpectBudget = sumExpectBudget;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "CountAndSumByMonthlyDTO{" +
                "fullYear=" + fullYear +
                ", monthly=" + monthly +
                ", allCampaigns=" + allCampaigns +
                ", childCampaigns=" + childCampaigns +
                ", olderCampaigns=" + olderCampaigns +
                ", etcCampaigns=" + etcCampaigns +
                ", animalCampaigns=" + animalCampaigns +
                ", natureCampaigns=" + natureCampaigns +
                ", sumCurrentBudget=" + sumCurrentBudget +
                ", sumGoalBudget=" + sumGoalBudget +
                ", displaySumCurrentBudget=" + displaySumCurrentBudget +
                ", sumExpectBudget=" + sumExpectBudget +
                ", progress=" + progress +
                '}';
    }
}
