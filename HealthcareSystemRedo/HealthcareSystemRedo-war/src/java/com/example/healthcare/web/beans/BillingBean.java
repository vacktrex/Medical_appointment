package com.example.healthcare.web.beans;

import com.example.healthcare.entities.Billing;
import com.example.healthcare.entities.Users;
import com.example.healthcare.sessionbeans.BillingFacade;
import com.example.healthcare.sessionbeans.UsersFacade;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean; // Keep this for BillingBean itself
import javax.faces.bean.ViewScoped;  // Keep this for BillingBean itself
import javax.faces.context.FacesContext;
import javax.inject.Inject; // NEW IMPORT: Import for @Inject

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import javax.servlet.http.HttpServletResponse;


@ManagedBean(name = "billingBean") // BillingBean still uses JSF ManagedBean
@ViewScoped
public class BillingBean implements Serializable {

    @EJB
    private BillingFacade billingFacade;
    @EJB
    private UsersFacade usersFacade;

    @Inject // NEW: Inject the LoginBean instance
    private LoginBean loginBean;

    private Integer patientUserId;
    private BigDecimal amount;
    private Date dateIssued = new Date();
    private String description;

    private String searchPatient;
    private String statusFilter;
    private Date startDate;
    private Date endDate;
    private List<Billing> filteredBills;

    private List<Billing> allBills;
    
    @PostConstruct
    public void init() {
        loadAllBills();
    }

    public String getSearchPatient() { return searchPatient; }
    public void setSearchPatient(String searchPatient) { this.searchPatient = searchPatient; }

    public String getStatusFilter() { return statusFilter; }
    public void setStatusFilter(String statusFilter) { this.statusFilter = statusFilter; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public List<Billing> getFilteredBills() {
        if (filteredBills == null) {
            return allBills; // fallback
        }
        return filteredBills;
    }

    public void applyFilters() {
        filteredBills = billingFacade.findAll().stream()
            .filter(b -> (searchPatient == null || searchPatient.isEmpty()
                        || b.getPatientId().getFullName().toLowerCase().contains(searchPatient.toLowerCase())))
            .filter(b -> (statusFilter == null || statusFilter.isEmpty()
                        || b.getStatus().equalsIgnoreCase(statusFilter)))
            .filter(b -> (startDate == null || !b.getDateIssued().before(startDate)))
            .filter(b -> (endDate == null || !b.getDateIssued().after(endDate)))
            .toList();
    }
    
    public void exportPdf() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Document document = new Document(PageSize.A4, 40, 40, 50, 60);
            context.responseComplete();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename=Bill_Report.pdf");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

            // Add watermark
            writer.setPageEvent(new PdfPageEventHelper() {
                @Override
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        PdfContentByte canvas = writer.getDirectContentUnder();
                        Font watermarkFont = new Font(Font.HELVETICA, 60, Font.BOLD, new Color(200, 200, 200));
                        Phrase watermark = new Phrase("CONFIDENTIAL", watermarkFont);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermark,
                            297.5f, 421, 45);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            document.open();

            // Logo (optional)
            try {
                String logoPath = context.getExternalContext().getRealPath("/resources/images/clinic_logo.png");
                Image logo = Image.getInstance(logoPath);
                logo.scaleAbsolute(60, 60);
                logo.setAlignment(Image.ALIGN_LEFT);
                document.add(logo);
            } catch (Exception e) {
                // logo optional
            }

            // Header
            Paragraph title = new Paragraph("HealthCare Clinic Billing Report", new Font(Font.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph address = new Paragraph("123 Health Avenue, Wellness City, 40000 Selangor", new Font(Font.HELVETICA, 10));
            address.setAlignment(Element.ALIGN_CENTER);
            address.setSpacingAfter(10f);
            document.add(address);

            Users doctor = loginBean.getLoggedInUser();
            Paragraph generatedBy = new Paragraph("Generated by: Dr. " + (doctor != null ? doctor.getFullName() : "Unknown"),
                    new Font(Font.HELVETICA, 10));
            generatedBy.setSpacingAfter(4f);
            document.add(generatedBy);

            Paragraph date = new Paragraph("Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                    new Font(Font.HELVETICA, 10));
            date.setSpacingAfter(12f);
            document.add(date);

            // Table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 2.5f, 2.5f, 1.5f, 2.0f, 3.0f, 1.5f});
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            String[] headers = {"Bill ID", "Patient", "Doctor", "Amount (RM)", "Date", "Description", "Status"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h));
                header.setBackgroundColor(new Color(230, 230, 250));
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(5);
                table.addCell(header);
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Billing bill : getFilteredBills()) {
                table.addCell(String.valueOf(bill.getBillId()));
                table.addCell(bill.getPatientId().getFullName());
                table.addCell(bill.getDoctorId().getFullName());
                table.addCell("RM " + bill.getAmount().toString());
                table.addCell(new java.text.SimpleDateFormat("yyyy-MM-dd").format(bill.getDateIssued()));
                table.addCell(bill.getDescription());
                table.addCell(bill.getStatus().toUpperCase());

                totalAmount = totalAmount.add(bill.getAmount());
            }

            document.add(table);

            // Total Amount
            Paragraph total = new Paragraph("Total Billed Amount: RM " + totalAmount.setScale(2, BigDecimal.ROUND_HALF_UP),
                    new Font(Font.HELVETICA, 12, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10f);
            document.add(total);

            // Footer / Signature
            Paragraph signature = new Paragraph("\n\n____________________________\nAuthorized Signature",
                    new Font(Font.HELVETICA, 10));
            signature.setAlignment(Element.ALIGN_RIGHT);
            signature.setSpacingBefore(40f);
            document.add(signature);

            document.close();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "PDF Export Failed", e.getMessage()));
            e.printStackTrace();
        }
    }




    public void exportCsv() {
    try {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().setResponseContentType("text/csv");
        context.getExternalContext().setResponseHeader("Content-Disposition", "attachment;filename=bills.csv");

        Writer responseWriter = context.getExternalContext().getResponseOutputWriter();
        PrintWriter writer = new PrintWriter(responseWriter);

        writer.println("Bill ID,Patient,Doctor,Amount,Date Issued,Status,Description");

        for (Billing b : getFilteredBills()) {
            writer.printf("%d,%s,%s,%.2f,%s,%s,%s\n",
                b.getBillId(),
                b.getPatientId().getFullName(),
                b.getDoctorId().getFullName(),
                b.getAmount(),
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(b.getDateIssued()),
                b.getStatus(),
                b.getDescription() != null ? b.getDescription().replace(",", " ") : ""
            );
        }

        writer.flush();
        context.responseComplete();
    } catch (IOException e) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Export failed: " + e.getMessage(), null));
        e.printStackTrace();
    }
}


    public List<Users> getPatients() {
        return usersFacade.findUsersByRole("patient");
    }

    public void loadAllBills() {
        allBills = billingFacade.findAll();
    }

    public void markBillAsPaid(Billing bill) {
        try {
            if (bill != null && !bill.getStatus().equals("paid")) {
                bill.setStatus("paid");
                billingFacade.edit(bill);
                loadAllBills();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bill ID " + bill.getBillId() + " marked as paid.", null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error marking bill as paid: " + e.getMessage(), null));
            e.printStackTrace();
        }
    }

    public String createBill() {
        try {
            Billing bill = new Billing();
            Users patient = usersFacade.find(patientUserId);

            // CHANGED LINE: Get the logged-in doctor directly from the injected LoginBean
            Users doctor = loginBean.getLoggedInUser();

            // Basic validation for doctor
            if (doctor == null) {
                System.out.println("DEBUG: LoggedInUser is NULL in injected LoginBean."); // Added debug for clarity
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Logged in doctor not found. Please log in again.", null));
                return null;
            } else {
                System.out.println("DEBUG: LoggedInUser found via injected LoginBean. User ID: " + doctor.getUserId() + ", Full Name: " + doctor.getFullName());
            }

            bill.setPatientId(patient);
            bill.setDoctorId(doctor);
            bill.setAmount(amount);
            bill.setDateIssued(dateIssued);
            bill.setDescription(description);
            bill.setStatus("unpaid");

            billingFacade.create(bill);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Bill created successfully!", null));

            // Reset the form fields
            patientUserId = null;
            amount = null;
            dateIssued = new Date();
            description = null;

            return "/doctor/dashboard.xhtml?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error creating bill: " + e.getMessage(), null));
            e.printStackTrace();
            return null;
        }
    }

    // --- Getters and setters for all fields (unchanged) ---
    public Integer getPatientUserId() { return patientUserId; }
    public void setPatientUserId(Integer patientUserId) { this.patientUserId = patientUserId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Date getDateIssued() { return dateIssued; }
    public void setDateIssued(Date dateIssued) { this.dateIssued = dateIssued; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Billing> getAllBills() { return allBills; }
    // No setter needed for allBills
}