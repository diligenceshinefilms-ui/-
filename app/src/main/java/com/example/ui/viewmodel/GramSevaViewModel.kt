package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.GramSevaRepository
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class GramSevaUiState(
  val citizen: CitizenProfile? = null,
  val notices: List<NoticeItem> = emptyList(),
  val complaints: List<ComplaintItem> = emptyList(),
  val bills: List<BillItem> = emptyList(),
  val certificates: List<CertificateApplication> = emptyList(),
  val developmentWorks: List<DevelopmentWork> = emptyList(),
  val gramSabhaMeetings: List<GramSabhaMeeting> = emptyList(),
  val schemes: List<GovScheme> = emptyList(),
  val emergencyContacts: List<EmergencyContact> = emptyList(),
  val diaryFeed: List<DailyDiaryFeed> = emptyList(),
  val language: AppLanguage = AppLanguage.MARATHI,
  val isAdminMode: Boolean = false,
  val currentTab: Int = 0,
  val complaintSearchQuery: String = "",
  val complaintCategoryFilter: String = "ALL",
  val servicesSubTab: Int = 0, // 0: Certificates, 1: Bills, 2: Works
  val noticesSubTab: Int = 0, // 0: Notices, 1: Gram Sabha, 2: Daily Diary
  val selectedComplaint: ComplaintItem? = null,
  val selectedBillForPayment: BillItem? = null,
  val selectedBillReceipt: BillItem? = null,
  val selectedCertificateForView: CertificateApplication? = null,
  val showNewComplaintDialog: Boolean = false,
  val showNewCertificateDialog: Boolean = false,
  val showAdminNoticeDialog: Boolean = false,
  val complaintToAdminUpdate: ComplaintItem? = null,
  val isProcessing: Boolean = false,
  val bannerNotification: String? = null
)

class GramSevaViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = GramSevaRepository(application)

  private val _uiState = MutableStateFlow(GramSevaUiState())
  val uiState: StateFlow<GramSevaUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      repository.ensureInitialized()
    }

    viewModelScope.launch {
      combine(
        repository.citizenProfile,
        repository.notices,
        repository.complaints,
        repository.bills,
        repository.certificates
      ) { citizen, notices, complaints, bills, certs ->
        _uiState.update { current ->
          current.copy(
            citizen = citizen,
            notices = notices,
            complaints = complaints,
            bills = bills,
            certificates = certs
          )
        }
      }.collect()
    }

    viewModelScope.launch {
      combine(
        repository.developmentWorks,
        repository.gramSabhaMeetings,
        repository.schemes,
        repository.emergencyContacts,
        repository.diaryFeed
      ) { works, meetings, schemes, contacts, diary ->
        _uiState.update { current ->
          current.copy(
            developmentWorks = works,
            gramSabhaMeetings = meetings,
            schemes = schemes,
            emergencyContacts = contacts,
            diaryFeed = diary
          )
        }
      }.collect()
    }
  }

  fun toggleLanguage() {
    _uiState.update {
      it.copy(language = if (it.language == AppLanguage.MARATHI) AppLanguage.ENGLISH else AppLanguage.MARATHI)
    }
  }

  fun toggleAdminMode() {
    val newMode = !_uiState.value.isAdminMode
    _uiState.update { it.copy(isAdminMode = newMode) }
    viewModelScope.launch {
      repository.toggleAdminMode(newMode)
    }
    showNotification(if (newMode) "प्रशासक मोड सुरू झाला (Admin Mode Activated)" else "नागरिक मोड सुरू झाला (Citizen Mode Activated)")
  }

  fun setTab(index: Int) {
    _uiState.update { it.copy(currentTab = index) }
  }

  fun setServicesSubTab(index: Int) {
    _uiState.update { it.copy(servicesSubTab = index) }
  }

  fun setNoticesSubTab(index: Int) {
    _uiState.update { it.copy(noticesSubTab = index) }
  }

  fun setComplaintSearch(query: String) {
    _uiState.update { it.copy(complaintSearchQuery = query) }
  }

  fun setComplaintCategoryFilter(cat: String) {
    _uiState.update { it.copy(complaintCategoryFilter = cat) }
  }

  // Dialog Toggles
  fun selectComplaint(complaint: ComplaintItem?) {
    _uiState.update { it.copy(selectedComplaint = complaint) }
  }

  fun openNewComplaintDialog(show: Boolean) {
    _uiState.update { it.copy(showNewComplaintDialog = show) }
  }

  fun openNewCertificateDialog(show: Boolean) {
    _uiState.update { it.copy(showNewCertificateDialog = show) }
  }

  fun selectBillForPayment(bill: BillItem?) {
    _uiState.update { it.copy(selectedBillForPayment = bill) }
  }

  fun selectBillReceipt(bill: BillItem?) {
    _uiState.update { it.copy(selectedBillReceipt = bill) }
  }

  fun selectCertificateForView(cert: CertificateApplication?) {
    _uiState.update { it.copy(selectedCertificateForView = cert) }
  }

  fun openAdminNoticeDialog(show: Boolean) {
    _uiState.update { it.copy(showAdminNoticeDialog = show) }
  }

  fun setComplaintToAdminUpdate(complaint: ComplaintItem?) {
    _uiState.update { it.copy(complaintToAdminUpdate = complaint) }
  }

  // Actions
  fun submitComplaint(
    category: String,
    subject: String,
    description: String,
    wardNumber: Int,
    locationText: String,
    citizenName: String,
    citizenMobile: String,
    photoType: String
  ) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      delay(400) // smooth feedback
      val complaint = repository.registerComplaint(
        category = category,
        subject = subject,
        description = description,
        wardNumber = wardNumber,
        locationText = locationText,
        citizenName = citizenName,
        citizenMobile = citizenMobile,
        photoPlaceholder = photoType
      )
      _uiState.update {
        it.copy(
          isProcessing = false,
          showNewComplaintDialog = false
        )
      }
      showNotification("तक्रार नोंदणी यशस्वी! तक्रार क्र.: ${complaint.complaintNumber}")
    }
  }

  fun processPayment(bill: BillItem, method: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      delay(600) // simulate banking verification
      val (success, receiptNo) = repository.payBill(bill.id, method)
      _uiState.update {
        it.copy(
          isProcessing = false,
          selectedBillForPayment = null
        )
      }
      if (success) {
        showNotification("कर भरणा यशस्वी! पावती क्रमांक: $receiptNo")
      }
    }
  }

  fun submitCertificateApplication(
    certType: String,
    applicantName: String,
    relativeName: String,
    proofType: String,
    proofNumber: String,
    purpose: String
  ) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      delay(400)
      val cert = repository.applyForCertificate(
        certificateType = certType,
        applicantName = applicantName,
        relativeName = relativeName,
        idProofType = proofType,
        idProofNumber = proofNumber,
        purpose = purpose
      )
      _uiState.update {
        it.copy(
          isProcessing = false,
          showNewCertificateDialog = false
        )
      }
      showNotification("दाखला अर्ज सादर केला! अर्ज क्र.: ${cert.applicationId}")
    }
  }

  // Admin Operations
  fun adminUpdateComplaint(complaintId: String, newStatus: String, officerRemark: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      repository.updateComplaintStatus(complaintId, newStatus, officerRemark)
      _uiState.update {
        it.copy(
          isProcessing = false,
          complaintToAdminUpdate = null
        )
      }
      showNotification("तक्रार स्थिती अद्ययावत केली: $newStatus")
    }
  }

  fun adminApproveCertificate(certId: String, remarks: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      repository.updateCertificateStatus(certId, "APPROVED", remarks)
      _uiState.update { it.copy(isProcessing = false) }
      showNotification("दाखला मंजूर करण्यात आला व डिजिटल स्वाक्षरी केली!")
    }
  }

  fun adminRejectCertificate(certId: String, remarks: String) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      repository.updateCertificateStatus(certId, "REJECTED", remarks)
      _uiState.update { it.copy(isProcessing = false) }
      showNotification("दाखला अर्ज नामंजूर करण्यात आला.")
    }
  }

  fun adminPublishNotice(
    titleMr: String,
    titleEn: String,
    category: String,
    priority: String,
    descriptionMr: String,
    descriptionEn: String
  ) {
    viewModelScope.launch {
      _uiState.update { it.copy(isProcessing = true) }
      repository.publishNotice(
        titleMr = titleMr,
        titleEn = titleEn,
        category = category,
        priority = priority,
        descriptionMr = descriptionMr,
        descriptionEn = descriptionEn
      )
      _uiState.update {
        it.copy(
          isProcessing = false,
          showAdminNoticeDialog = false
        )
      }
      showNotification("नवीन सूचना गावात प्रसिद्ध करण्यात आली!")
    }
  }

  fun adminUpdateWorkProgress(workId: String, newProgress: Int, newStatus: String) {
    viewModelScope.launch {
      repository.updateWorkProgress(workId, newProgress, newStatus)
      showNotification("विकासकामाची प्रगती $newProgress% अद्ययावत केली!")
    }
  }

  fun showNotification(message: String) {
    _uiState.update { it.copy(bannerNotification = message) }
    viewModelScope.launch {
      delay(4000)
      _uiState.update {
        if (it.bannerNotification == message) it.copy(bannerNotification = null) else it
      }
    }
  }

  fun dismissNotification() {
    _uiState.update { it.copy(bannerNotification = null) }
  }
}
