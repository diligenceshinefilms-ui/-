package com.example.data.repository

import android.content.Context
import com.example.data.local.GramSevaDatabase
import com.example.data.local.seedInitialData
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class GramSevaRepository(context: Context) {
  private val database = GramSevaDatabase.getDatabase(context)
  private val citizenDao = database.citizenDao()
  private val noticeDao = database.noticeDao()
  private val complaintDao = database.complaintDao()
  private val billDao = database.billDao()
  private val certificateDao = database.certificateDao()
  private val workDao = database.developmentWorkDao()
  private val gramSabhaDao = database.gramSabhaDao()
  private val schemeDao = database.schemeDao()
  private val contactDao = database.contactDao()
  private val diaryDao = database.diaryDao()

  suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
    val existing = citizenDao.getCitizen().firstOrNull()
    if (existing == null) {
      seedInitialData(database)
    }
  }

  // Flows
  val citizenProfile: Flow<CitizenProfile?> = citizenDao.getCitizen()
  val notices: Flow<List<NoticeItem>> = noticeDao.getAllNotices()
  val complaints: Flow<List<ComplaintItem>> = complaintDao.getAllComplaints()
  val bills: Flow<List<BillItem>> = billDao.getAllBills()
  val certificates: Flow<List<CertificateApplication>> = certificateDao.getAllCertificates()
  val developmentWorks: Flow<List<DevelopmentWork>> = workDao.getAllWorks()
  val gramSabhaMeetings: Flow<List<GramSabhaMeeting>> = gramSabhaDao.getAllMeetings()
  val schemes: Flow<List<GovScheme>> = schemeDao.getAllSchemes()
  val emergencyContacts: Flow<List<EmergencyContact>> = contactDao.getAllContacts()
  val diaryFeed: Flow<List<DailyDiaryFeed>> = diaryDao.getDiaryEntries()

  // Citizen Profile
  suspend fun updateCitizen(profile: CitizenProfile) = withContext(Dispatchers.IO) {
    citizenDao.updateCitizen(profile)
  }

  suspend fun toggleAdminMode(enableAdmin: Boolean) = withContext(Dispatchers.IO) {
    val current = citizenDao.getCitizen().firstOrNull()
    if (current != null) {
      citizenDao.updateCitizen(current.copy(isAdmin = enableAdmin))
    }
  }

  // Register Complaint
  suspend fun registerComplaint(
    category: String,
    subject: String,
    description: String,
    wardNumber: Int,
    locationText: String,
    citizenName: String,
    citizenMobile: String,
    photoPlaceholder: String = ""
  ): ComplaintItem = withContext(Dispatchers.IO) {
    val randomCode = (1000..9999).random()
    val complaintNumber = "GP-2026-$randomCode"
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("mr", "IN"))
    val todayDate = dateFormat.format(Date())

    val newComplaint = ComplaintItem(
      id = UUID.randomUUID().toString(),
      complaintNumber = complaintNumber,
      category = category,
      subject = subject,
      description = description,
      citizenName = citizenName,
      citizenMobile = citizenMobile,
      wardNumber = wardNumber,
      locationText = locationText,
      status = "SUBMITTED",
      submittedDate = todayDate,
      assignedOfficer = "तक्रार निवारण कक्ष (प्रलंबित वाटप)",
      officerRemark = "तक्रार प्रणालीमध्ये यशस्वीरित्या दाखल झाली आहे. संबंधित विभागाकडे वर्ग केली जात आहे.",
      photoPlaceholder = photoPlaceholder
    )
    complaintDao.insertComplaint(newComplaint)
    newComplaint
  }

  // Pay Bill
  suspend fun payBill(
    billId: String,
    paymentMethod: String
  ): Pair<Boolean, String> = withContext(Dispatchers.IO) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale("mr", "IN"))
    val todayDate = dateFormat.format(Date())
    val txnId = "TXN" + System.currentTimeMillis().toString().takeLast(10)
    val rcpNumber = "GP-RCP-2026-" + (2000..8999).random()

    billDao.markAsPaid(
      id = billId,
      paidDate = todayDate,
      txnId = txnId,
      rcpNumber = rcpNumber
    )
    Pair(true, rcpNumber)
  }

  // Certificate Application
  suspend fun applyForCertificate(
    certificateType: String,
    applicantName: String,
    relativeName: String,
    idProofType: String,
    idProofNumber: String,
    purpose: String
  ): CertificateApplication = withContext(Dispatchers.IO) {
    val randomId = (100..999).random()
    val appId = "CRT-2026-$randomId"
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("mr", "IN"))
    val todayDate = dateFormat.format(Date())

    val newCert = CertificateApplication(
      id = UUID.randomUUID().toString(),
      applicationId = appId,
      certificateType = certificateType,
      applicantName = applicantName,
      relativeName = relativeName,
      idProofType = idProofType,
      idProofNumber = idProofNumber,
      purpose = purpose,
      status = "PENDING",
      appliedDate = todayDate,
      remarks = "अर्ज छाननी व तलाठी/ग्रामसेवक पडताळणी प्रक्रियेत आहे."
    )
    certificateDao.insertCertificate(newCert)
    newCert
  }

  // Admin Actions
  suspend fun updateComplaintStatus(
    complaintId: String,
    newStatus: String,
    officerRemark: String
  ) = withContext(Dispatchers.IO) {
    val resolvedDate = if (newStatus == "RESOLVED" || newStatus == "CLOSED") {
      SimpleDateFormat("dd MMMM yyyy", Locale("mr", "IN")).format(Date())
    } else ""
    complaintDao.updateStatus(complaintId, newStatus, officerRemark, resolvedDate)
  }

  suspend fun updateCertificateStatus(
    certId: String,
    newStatus: String,
    remarks: String
  ) = withContext(Dispatchers.IO) {
    val approvedDate = if (newStatus == "APPROVED") {
      SimpleDateFormat("dd MMMM yyyy", Locale("mr", "IN")).format(Date())
    } else ""
    val verificationCode = if (newStatus == "APPROVED") {
      "GP-AND-VER-" + (10000..99999).random()
    } else ""

    certificateDao.updateStatus(certId, newStatus, approvedDate, remarks, verificationCode)
  }

  suspend fun publishNotice(
    titleMr: String,
    titleEn: String,
    category: String,
    priority: String,
    descriptionMr: String,
    descriptionEn: String
  ) = withContext(Dispatchers.IO) {
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("mr", "IN"))
    val newNotice = NoticeItem(
      id = UUID.randomUUID().toString(),
      titleMr = titleMr,
      titleEn = titleEn,
      category = category,
      date = dateFormat.format(Date()),
      priority = priority,
      descriptionMr = descriptionMr,
      descriptionEn = descriptionEn,
      attachmentName = "GP_Official_Notice.pdf"
    )
    noticeDao.insertNotice(newNotice)
  }

  suspend fun updateWorkProgress(
    workId: String,
    progress: Int,
    status: String
  ) = withContext(Dispatchers.IO) {
    workDao.updateProgress(workId, progress, status)
  }
}
