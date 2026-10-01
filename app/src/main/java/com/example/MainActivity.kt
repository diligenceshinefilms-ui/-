package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.*
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: GramSevaViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val lang = uiState.language

        // BackHandler to handle subtabs or return to home
        BackHandler(enabled = uiState.currentTab != 0) {
          viewModel.setTab(0)
        }

        Scaffold(
          topBar = {
            TopAppBar(
              title = {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  GramPanchayatEmblem(size = 36)
                  Column {
                    Text(
                      text = GramSevaStrings.appTitle(lang),
                      fontSize = 18.sp,
                      fontWeight = FontWeight.ExtraBold,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                      text = "ग्रामपंचायत निचपूर • किनवट",
                      fontSize = 11.sp,
                      color = GreenPrimary,
                      fontWeight = FontWeight.SemiBold
                    )
                  }
                }
              },
              actions = {
                // Language Toggle Button
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = GreenContainer,
                  modifier = Modifier
                    .padding(end = 6.dp)
                    .clickable { viewModel.toggleLanguage() }
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.Language,
                      contentDescription = "Language",
                      tint = GreenPrimary,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = if (lang == AppLanguage.MARATHI) "मराठी" else "English",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = GreenPrimary
                    )
                  }
                }

                // Admin Toggle Chip
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = if (uiState.isAdminMode) SaffronContainer else MaterialTheme.colorScheme.surfaceVariant,
                  modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { viewModel.toggleAdminMode() }
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Icon(
                      imageVector = if (uiState.isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                      contentDescription = "Role Mode",
                      tint = if (uiState.isAdminMode) SaffronSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(16.dp)
                    )
                    Text(
                      text = if (uiState.isAdminMode) "अधिकारी" else "नागरिक",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = if (uiState.isAdminMode) OnSaffronContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }
              },
              colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
              )
            )
          },
          bottomBar = {
            NavigationBar(
              containerColor = MaterialTheme.colorScheme.surface,
              tonalElevation = 8.dp
            ) {
              val navItems = listOf(
                Triple(0, GramSevaStrings.tabHome(lang), Icons.Default.Home),
                Triple(1, GramSevaStrings.tabServices(lang), Icons.Default.ReceiptLong),
                Triple(2, GramSevaStrings.tabComplaints(lang), Icons.Default.ReportProblem),
                Triple(3, GramSevaStrings.tabSabha(lang), Icons.Default.Campaign),
                Triple(4, GramSevaStrings.tabMore(lang), Icons.Default.MoreHoriz)
              )

              navItems.forEach { (index, title, icon) ->
                val isSelected = uiState.currentTab == index
                NavigationBarItem(
                  selected = isSelected,
                  onClick = { viewModel.setTab(index) },
                  icon = {
                    Icon(
                      imageVector = icon,
                      contentDescription = title,
                      tint = if (isSelected) GreenPrimary else TextSecondary
                    )
                  },
                  label = {
                    Text(
                      text = title,
                      fontSize = 10.sp,
                      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                      color = if (isSelected) GreenPrimary else TextSecondary,
                      maxLines = 1
                    )
                  },
                  colors = NavigationBarItemDefaults.colors(
                    indicatorColor = GreenContainer
                  )
                )
              }
            }
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            // Main Screen Display
            when (uiState.currentTab) {
              0 -> HomeScreen(
                uiState = uiState,
                onNavigateToTab = { tab -> viewModel.setTab(tab) },
                onOpenComplaintDialog = { viewModel.openNewComplaintDialog(true) },
                onSelectNotice = { /* Open Notice */ }
              )
              1 -> ServicesScreen(
                uiState = uiState,
                onSubTabChange = { subTab -> viewModel.setServicesSubTab(subTab) },
                onOpenNewCertificate = { viewModel.openNewCertificateDialog(true) },
                onSelectCertificate = { cert -> viewModel.selectCertificateForView(cert) },
                onSelectBillToPay = { bill -> viewModel.selectBillForPayment(bill) },
                onSelectBillReceipt = { bill -> viewModel.selectBillReceipt(bill) }
              )
              2 -> ComplaintsScreen(
                uiState = uiState,
                onSearchChange = { q -> viewModel.setComplaintSearch(q) },
                onCategoryFilterChange = { cat -> viewModel.setComplaintCategoryFilter(cat) },
                onSelectComplaint = { comp -> viewModel.selectComplaint(comp) },
                onOpenNewComplaint = { viewModel.openNewComplaintDialog(true) }
              )
              3 -> NoticesScreen(
                uiState = uiState,
                onSubTabChange = { subTab -> viewModel.setNoticesSubTab(subTab) },
                onOpenNoticeAdmin = { viewModel.openAdminNoticeDialog(true) }
              )
              4 -> MoreAndAdminScreen(
                uiState = uiState,
                onToggleAdmin = { viewModel.toggleAdminMode() },
                onOpenAdminComplaintUpdate = { comp -> viewModel.setComplaintToAdminUpdate(comp) },
                onAdminApproveCert = { id, remark -> viewModel.adminApproveCertificate(id, remark) },
                onAdminRejectCert = { id, remark -> viewModel.adminRejectCertificate(id, remark) },
                onAdminOpenNotice = { viewModel.openAdminNoticeDialog(true) },
                onAdminUpdateProgress = { id, prog, stat -> viewModel.adminUpdateWorkProgress(id, prog, stat) }
              )
            }

            // High-Visibility Feedback Banner
            AnimatedVisibility(
              visible = uiState.bannerNotification != null,
              enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
              exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
              modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
            ) {
              uiState.bannerNotification?.let { msg ->
                Surface(
                  shape = RoundedCornerShape(12.dp),
                  color = GreenPrimary,
                  shadowElevation = 6.dp,
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.dismissNotification() }
                ) {
                  Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Default.CheckCircle,
                      contentDescription = null,
                      tint = Color.White
                    )
                    Text(
                      text = msg,
                      color = Color.White,
                      fontSize = 13.sp,
                      fontWeight = FontWeight.SemiBold,
                      modifier = Modifier.weight(1f)
                    )
                    Icon(
                      imageVector = Icons.Default.Close,
                      contentDescription = "Dismiss",
                      tint = Color.White.copy(alpha = 0.8f),
                      modifier = Modifier.size(18.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // Modals & Dialogs
        uiState.selectedComplaint?.let { complaint ->
          ComplaintDetailModal(
            complaint = complaint,
            language = lang,
            isAdmin = uiState.isAdminMode,
            onDismiss = { viewModel.selectComplaint(null) },
            onOpenAdminUpdate = { comp ->
              viewModel.selectComplaint(null)
              viewModel.setComplaintToAdminUpdate(comp)
            }
          )
        }

        if (uiState.showNewComplaintDialog) {
          NewComplaintModal(
            language = lang,
            isProcessing = uiState.isProcessing,
            onDismiss = { viewModel.openNewComplaintDialog(false) },
            onSubmit = { cat, sub, desc, ward, loc, name, mob, photo ->
              viewModel.submitComplaint(cat, sub, desc, ward, loc, name, mob, photo)
            }
          )
        }

        uiState.selectedBillForPayment?.let { bill ->
          TaxPaymentModal(
            bill = bill,
            language = lang,
            isProcessing = uiState.isProcessing,
            onDismiss = { viewModel.selectBillForPayment(null) },
            onConfirmPay = { b, method ->
              viewModel.processPayment(b, method)
            }
          )
        }

        uiState.selectedBillReceipt?.let { bill ->
          OfficialReceiptModal(
            bill = bill,
            language = lang,
            onDismiss = { viewModel.selectBillReceipt(null) }
          )
        }

        uiState.selectedCertificateForView?.let { cert ->
          DigitalCertificateModal(
            cert = cert,
            language = lang,
            onDismiss = { viewModel.selectCertificateForView(null) }
          )
        }

        if (uiState.showNewCertificateDialog) {
          NewCertificateModal(
            language = lang,
            isProcessing = uiState.isProcessing,
            onDismiss = { viewModel.openNewCertificateDialog(false) },
            onSubmit = { type, applicant, relative, proofType, proofNum, purpose ->
              viewModel.submitCertificateApplication(type, applicant, relative, proofType, proofNum, purpose)
            }
          )
        }

        uiState.complaintToAdminUpdate?.let { comp ->
          AdminUpdateComplaintModal(
            complaint = comp,
            language = lang,
            isProcessing = uiState.isProcessing,
            onDismiss = { viewModel.setComplaintToAdminUpdate(null) },
            onSubmit = { id, stat, remark ->
              viewModel.adminUpdateComplaint(id, stat, remark)
            }
          )
        }

        if (uiState.showAdminNoticeDialog) {
          AdminPublishNoticeModal(
            language = lang,
            isProcessing = uiState.isProcessing,
            onDismiss = { viewModel.openAdminNoticeDialog(false) },
            onPublish = { tMr, tEn, cat, prio, dMr, dEn ->
              viewModel.adminPublishNotice(tMr, tEn, cat, prio, dMr, dEn)
            }
          )
        }
      }
    }
  }
}
