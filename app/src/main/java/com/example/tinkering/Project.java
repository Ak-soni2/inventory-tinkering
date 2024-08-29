package com.example.tinkering;

public class Project {
    private String pdfUrl;
    private String projectTitle;
    private String status; // New field to store the status of the project

    public Project() {
        // Default constructor required for calls to DataSnapshot.getValue(Project.class)
    }

    public Project(String pdfUrl, String projectTitle, String status) {
        this.pdfUrl = pdfUrl;
        this.projectTitle = projectTitle;
        this.status = status;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
