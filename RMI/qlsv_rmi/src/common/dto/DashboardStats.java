package common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardStats implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> enrollmentByYear = new LinkedHashMap<>();
    private final Map<String, Integer> genderDistribution = new LinkedHashMap<>();
    private final Map<String, Integer> statusBreakdown = new LinkedHashMap<>();
    private final List<DashboardClassSummary> topClasses = new ArrayList<>();
    private Date generatedAt;

    public Map<String, Integer> getEnrollmentByYear() {
        return enrollmentByYear;
    }

    public Map<String, Integer> getGenderDistribution() {
        return genderDistribution;
    }

    public Map<String, Integer> getStatusBreakdown() {
        return statusBreakdown;
    }

    public List<DashboardClassSummary> getTopClasses() {
        return topClasses;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Date generatedAt) {
        this.generatedAt = generatedAt;
    }

    public void clear() {
        enrollmentByYear.clear();
        genderDistribution.clear();
        statusBreakdown.clear();
        topClasses.clear();
    }
}
