package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CitizenDao {
  @Query("SELECT * FROM citizen_profile LIMIT 1")
  fun getCitizen(): Flow<CitizenProfile?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCitizen(profile: CitizenProfile)

  @Update
  suspend fun updateCitizen(profile: CitizenProfile)
}

@Dao
interface NoticeDao {
  @Query("SELECT * FROM notices ORDER BY id DESC")
  fun getAllNotices(): Flow<List<NoticeItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(notices: List<NoticeItem>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotice(notice: NoticeItem)

  @Query("DELETE FROM notices WHERE id = :id")
  suspend fun deleteNotice(id: String)
}

@Dao
interface ComplaintDao {
  @Query("SELECT * FROM complaints ORDER BY id DESC")
  fun getAllComplaints(): Flow<List<ComplaintItem>>

  @Query("SELECT * FROM complaints WHERE id = :id")
  suspend fun getComplaintById(id: String): ComplaintItem?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertComplaint(complaint: ComplaintItem)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(complaints: List<ComplaintItem>)

  @Update
  suspend fun updateComplaint(complaint: ComplaintItem)

  @Query("UPDATE complaints SET status = :newStatus, officerRemark = :remark, resolvedDate = :resolvedDate WHERE id = :id")
  suspend fun updateStatus(id: String, newStatus: String, remark: String, resolvedDate: String)
}

@Dao
interface BillDao {
  @Query("SELECT * FROM bills ORDER BY id ASC")
  fun getAllBills(): Flow<List<BillItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(bills: List<BillItem>)

  @Update
  suspend fun updateBill(bill: BillItem)

  @Query("UPDATE bills SET isPaid = 1, paidDate = :paidDate, transactionId = :txnId, receiptNumber = :rcpNumber WHERE id = :id")
  suspend fun markAsPaid(id: String, paidDate: String, txnId: String, rcpNumber: String)
}

@Dao
interface CertificateDao {
  @Query("SELECT * FROM certificates ORDER BY id DESC")
  fun getAllCertificates(): Flow<List<CertificateApplication>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertCertificate(cert: CertificateApplication)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(certs: List<CertificateApplication>)

  @Query("UPDATE certificates SET status = :newStatus, approvedDate = :approvedDate, remarks = :remarks, verificationCode = :code WHERE id = :id")
  suspend fun updateStatus(id: String, newStatus: String, approvedDate: String, remarks: String, code: String)
}

@Dao
interface DevelopmentWorkDao {
  @Query("SELECT * FROM development_works ORDER BY progressPercent DESC")
  fun getAllWorks(): Flow<List<DevelopmentWork>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(works: List<DevelopmentWork>)

  @Query("UPDATE development_works SET progressPercent = :progress, status = :status WHERE id = :id")
  suspend fun updateProgress(id: String, progress: Int, status: String)
}

@Dao
interface GramSabhaDao {
  @Query("SELECT * FROM gram_sabha ORDER BY meetingDate DESC")
  fun getAllMeetings(): Flow<List<GramSabhaMeeting>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(meetings: List<GramSabhaMeeting>)
}

@Dao
interface SchemeDao {
  @Query("SELECT * FROM schemes ORDER BY id ASC")
  fun getAllSchemes(): Flow<List<GovScheme>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(schemes: List<GovScheme>)
}

@Dao
interface ContactDao {
  @Query("SELECT * FROM contacts ORDER BY isEmergency DESC, id ASC")
  fun getAllContacts(): Flow<List<EmergencyContact>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(contacts: List<EmergencyContact>)
}

@Dao
interface DiaryDao {
  @Query("SELECT * FROM diary_feed ORDER BY id DESC")
  fun getDiaryEntries(): Flow<List<DailyDiaryFeed>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(entries: List<DailyDiaryFeed>)
}
