package com.devansh.noteapp.core.designsystem.utils

//import com.yangdai.opennote.presentation.util.Constants
//import com.yangdai.opennote.presentation.util.encryptBackupData
//import com.yangdai.opennote.presentation.util.getOrCreateDirectory
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.withContext
//import kotlinx.serialization.json.Json
import noteapp.shared.core.design_system.generated.resources.Res
import noteapp.shared.core.design_system.generated.resources.daily
import noteapp.shared.core.design_system.generated.resources.monthly
import noteapp.shared.core.design_system.generated.resources.never
import noteapp.shared.core.design_system.generated.resources.weekly
import org.jetbrains.compose.resources.Resource

enum class BackupFrequency(val days: Int, val textRes: Resource) {
    NEVER(0, Res.string.never),
    DAILY(1, Res.string.daily),
    WEEKLY(7, Res.string.weekly),
    MONTHLY(30, Res.string.monthly);
}

class BackupWorker()
//    appContext: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(appContext, workerParams) {
//){
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        try {
//            val context = applicationContext
//            val dataStoreRepository = AppModule.provideAppDataStoreRepository(context)
//            val database = AppModule.provideNoteDatabase(context)
//            val noteRepository = AppModule.provideNoteRepository(database)
//            val folderRepository = AppModule.provideFolderRepository(database)
//            val useCases = AppModule.provideNoteUseCases(noteRepository, folderRepository)
//
//            val rootUri =
//                dataStoreRepository.getStringValue(Constants.Preferences.STORAGE_PATH, "")
//                    .toUri()
//            // 获取Open Note目录
//            val openNoteDir =
//                getOrCreateDirectory(context, rootUri, Constants.File.OPENNOTE)
//            // 获取Backup目录
//            val backupDir = openNoteDir?.let { dir ->
//                getOrCreateDirectory(context, diRes.uri, Constants.File.OPENNOTE_BACKUP)
//            }
//            backupDir?.let { dir ->
//                val notes = useCases.getNotes().first()
//                val folders = useCases.getFolders().first()
//                val backupData = BackupData(notes, folders)
//                val json = Json.encodeToString(backupData)
//                val encryptedJson = encryptBackupData(json)
//
//                val fileName = "${System.currentTimeMillis()}.json"
//                val file = diRes.createFile("application/json", fileName)
//
//                file?.let { docFile ->
//                    context.contentResolveRes.openOutputStream(docFile.uri)
//                        ?.use { outputStream ->
//                            OutputStreamWriter(outputStream).use { writer ->
//                                writeRes.write(encryptedJson)
//                            }
//                        }
//                }
//            }
//
//            Result.success()
//        } catch (_: Exception) {
//            Result.failure()
//        }
//    }
//}
