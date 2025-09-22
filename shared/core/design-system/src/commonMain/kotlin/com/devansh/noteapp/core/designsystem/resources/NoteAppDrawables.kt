@file:Suppress("unused")

package com.devansh.noteapp.core.designsystem.resources

import noteapp.shared.core.design_system.generated.resources.Res
import noteapp.shared.core.design_system.generated.resources.alert
import noteapp.shared.core.design_system.generated.resources.all_notes
import noteapp.shared.core.design_system.generated.resources.app_info
import noteapp.shared.core.design_system.generated.resources.apparel_24px
import noteapp.shared.core.design_system.generated.resources.ascending
import noteapp.shared.core.design_system.generated.resources.ask_save_note
import noteapp.shared.core.design_system.generated.resources.audio
import noteapp.shared.core.design_system.generated.resources.auto_save
import noteapp.shared.core.design_system.generated.resources.backup
import noteapp.shared.core.design_system.generated.resources.backup_description
import noteapp.shared.core.design_system.generated.resources.biometric
import noteapp.shared.core.design_system.generated.resources.bold
import noteapp.shared.core.design_system.generated.resources.cancel
import noteapp.shared.core.design_system.generated.resources.card_size
import noteapp.shared.core.design_system.generated.resources.categories
import noteapp.shared.core.design_system.generated.resources.category
import noteapp.shared.core.design_system.generated.resources.caution_alert
import noteapp.shared.core.design_system.generated.resources.center
import noteapp.shared.core.design_system.generated.resources.change
import noteapp.shared.core.design_system.generated.resources.char_count
import noteapp.shared.core.design_system.generated.resources.checked
import noteapp.shared.core.design_system.generated.resources.clean
import noteapp.shared.core.design_system.generated.resources.clip
import noteapp.shared.core.design_system.generated.resources.cloud
import noteapp.shared.core.design_system.generated.resources.code
import noteapp.shared.core.design_system.generated.resources.color
import noteapp.shared.core.design_system.generated.resources.color_picker
import noteapp.shared.core.design_system.generated.resources.color_platte
import noteapp.shared.core.design_system.generated.resources.column
import noteapp.shared.core.design_system.generated.resources.compact
import noteapp.shared.core.design_system.generated.resources.compose
import noteapp.shared.core.design_system.generated.resources.compose_multiplatform
import noteapp.shared.core.design_system.generated.resources.content
import noteapp.shared.core.design_system.generated.resources.create_note
import noteapp.shared.core.design_system.generated.resources.create_password
import noteapp.shared.core.design_system.generated.resources.create_text_file
import noteapp.shared.core.design_system.generated.resources.current_root_directory_location_of_the_file_repository
import noteapp.shared.core.design_system.generated.resources.daily
import noteapp.shared.core.design_system.generated.resources.dark
import noteapp.shared.core.design_system.generated.resources.dark_mode
import noteapp.shared.core.design_system.generated.resources.data
import noteapp.shared.core.design_system.generated.resources.date
import noteapp.shared.core.design_system.generated.resources.date_format
import noteapp.shared.core.design_system.generated.resources.date_in_the_template_file_will_be_replaced_with_this_value
import noteapp.shared.core.design_system.generated.resources.default_editing_mode
import noteapp.shared.core.design_system.generated.resources.default_editing_mode_for_note
import noteapp.shared.core.design_system.generated.resources.default_size
import noteapp.shared.core.design_system.generated.resources.default_view
import noteapp.shared.core.design_system.generated.resources.default_view_for_note
import noteapp.shared.core.design_system.generated.resources.delete
import noteapp.shared.core.design_system.generated.resources.delete_all
import noteapp.shared.core.design_system.generated.resources.deleting_a_folder_will_also_delete_all_the_notes_it_contains_and_they_cannot_be_restored_do_you_want_to_continue
import noteapp.shared.core.design_system.generated.resources.descending
import noteapp.shared.core.design_system.generated.resources.destination_folder
import noteapp.shared.core.design_system.generated.resources.display_style
import noteapp.shared.core.design_system.generated.resources.dynamic_only_android_12
import noteapp.shared.core.design_system.generated.resources.edit_text_file
import noteapp.shared.core.design_system.generated.resources.edited
import noteapp.shared.core.design_system.generated.resources.editing_view
import noteapp.shared.core.design_system.generated.resources.editor
import noteapp.shared.core.design_system.generated.resources.editor_description
import noteapp.shared.core.design_system.generated.resources.ellipsis
import noteapp.shared.core.design_system.generated.resources.enter_again
import noteapp.shared.core.design_system.generated.resources.explore_and_share_community_note_templates
import noteapp.shared.core.design_system.generated.resources.export
import noteapp.shared.core.design_system.generated.resources.export_as
import noteapp.shared.core.design_system.generated.resources.file
import noteapp.shared.core.design_system.generated.resources.files_in_this_folder_will_be_available_as_templates
import noteapp.shared.core.design_system.generated.resources.find
import noteapp.shared.core.design_system.generated.resources.flat
import noteapp.shared.core.design_system.generated.resources.folders
import noteapp.shared.core.design_system.generated.resources.font_size
import noteapp.shared.core.design_system.generated.resources.font_size_detail
import noteapp.shared.core.design_system.generated.resources.for_more_syntax_refer_to
import noteapp.shared.core.design_system.generated.resources.format_reference
import noteapp.shared.core.design_system.generated.resources.free_up_detail
import noteapp.shared.core.design_system.generated.resources.free_up_space
import noteapp.shared.core.design_system.generated.resources.frequency_backup
import noteapp.shared.core.design_system.generated.resources.geomanist_medium
import noteapp.shared.core.design_system.generated.resources.geomanist_regular
import noteapp.shared.core.design_system.generated.resources.guide
import noteapp.shared.core.design_system.generated.resources.horizontal_rule
import noteapp.shared.core.design_system.generated.resources.ic_menu_copy
import noteapp.shared.core.design_system.generated.resources.ic_menu_delete
import noteapp.shared.core.design_system.generated.resources.ic_menu_edit
import noteapp.shared.core.design_system.generated.resources.ic_menu_share
import noteapp.shared.core.design_system.generated.resources.image
import noteapp.shared.core.design_system.generated.resources.import_files
import noteapp.shared.core.design_system.generated.resources.import_files_description
import noteapp.shared.core.design_system.generated.resources.important_alert
import noteapp.shared.core.design_system.generated.resources.incorrect_link_format
import noteapp.shared.core.design_system.generated.resources.italic
import noteapp.shared.core.design_system.generated.resources.language
import noteapp.shared.core.design_system.generated.resources.language_description
import noteapp.shared.core.design_system.generated.resources.large
import noteapp.shared.core.design_system.generated.resources.left
import noteapp.shared.core.design_system.generated.resources.light
import noteapp.shared.core.design_system.generated.resources.line_count
import noteapp.shared.core.design_system.generated.resources.line_numbers
import noteapp.shared.core.design_system.generated.resources.lines
import noteapp.shared.core.design_system.generated.resources.link
import noteapp.shared.core.design_system.generated.resources.lint
import noteapp.shared.core.design_system.generated.resources.lint_description
import noteapp.shared.core.design_system.generated.resources.list
import noteapp.shared.core.design_system.generated.resources.lite_mode
import noteapp.shared.core.design_system.generated.resources.manage_folders
import noteapp.shared.core.design_system.generated.resources.mark
import noteapp.shared.core.design_system.generated.resources.markdown_copy
import noteapp.shared.core.design_system.generated.resources.math
import noteapp.shared.core.design_system.generated.resources.medium
import noteapp.shared.core.design_system.generated.resources.mermaid_diagram
import noteapp.shared.core.design_system.generated.resources.mode
import noteapp.shared.core.design_system.generated.resources.modify
import noteapp.shared.core.design_system.generated.resources.monthly
import noteapp.shared.core.design_system.generated.resources.montserratbold
import noteapp.shared.core.design_system.generated.resources.move
import noteapp.shared.core.design_system.generated.resources.name
import noteapp.shared.core.design_system.generated.resources.navigate_back
import noteapp.shared.core.design_system.generated.resources.never
import noteapp.shared.core.design_system.generated.resources.no_calendar_app_found
import noteapp.shared.core.design_system.generated.resources.no_conversation
import noteapp.shared.core.design_system.generated.resources.no_conversation_light
import noteapp.shared.core.design_system.generated.resources.no_notes
import noteapp.shared.core.design_system.generated.resources.no_password_set
import noteapp.shared.core.design_system.generated.resources.no_results
import noteapp.shared.core.design_system.generated.resources.no_results_light
import noteapp.shared.core.design_system.generated.resources.no_task
import noteapp.shared.core.design_system.generated.resources.no_task_light
import noteapp.shared.core.design_system.generated.resources.note
import noteapp.shared.core.design_system.generated.resources.note_alert
import noteapp.shared.core.design_system.generated.resources.note_editing
import noteapp.shared.core.design_system.generated.resources.notes
import noteapp.shared.core.design_system.generated.resources.ok
import noteapp.shared.core.design_system.generated.resources.onboard1
import noteapp.shared.core.design_system.generated.resources.onboard2
import noteapp.shared.core.design_system.generated.resources.onboard3
import noteapp.shared.core.design_system.generated.resources.open_file_manager
import noteapp.shared.core.design_system.generated.resources.outline
import noteapp.shared.core.design_system.generated.resources.overview
import noteapp.shared.core.design_system.generated.resources.paragraph_count
import noteapp.shared.core.design_system.generated.resources.pass
import noteapp.shared.core.design_system.generated.resources.password
import noteapp.shared.core.design_system.generated.resources.password_description
import noteapp.shared.core.design_system.generated.resources.preview
import noteapp.shared.core.design_system.generated.resources.print
import noteapp.shared.core.design_system.generated.resources.privacy_policy
import noteapp.shared.core.design_system.generated.resources.progress
import noteapp.shared.core.design_system.generated.resources.quote
import noteapp.shared.core.design_system.generated.resources.rate_this_app
import noteapp.shared.core.design_system.generated.resources.raw
import noteapp.shared.core.design_system.generated.resources.reading_view
import noteapp.shared.core.design_system.generated.resources.record_audio
import noteapp.shared.core.design_system.generated.resources.recovery
import noteapp.shared.core.design_system.generated.resources.redo
import noteapp.shared.core.design_system.generated.resources.remind
import noteapp.shared.core.design_system.generated.resources.replace
import noteapp.shared.core.design_system.generated.resources.report_a_bug_or_request_a_feature
import noteapp.shared.core.design_system.generated.resources.reset
import noteapp.shared.core.design_system.generated.resources.reset_database
import noteapp.shared.core.design_system.generated.resources.reset_database_description
import noteapp.shared.core.design_system.generated.resources.reset_database_warning
import noteapp.shared.core.design_system.generated.resources.restore
import noteapp.shared.core.design_system.generated.resources.restore_all
import noteapp.shared.core.design_system.generated.resources.restore_description
import noteapp.shared.core.design_system.generated.resources.right
import noteapp.shared.core.design_system.generated.resources.root_directory_location
import noteapp.shared.core.design_system.generated.resources.row
import noteapp.shared.core.design_system.generated.resources.sample_note
import noteapp.shared.core.design_system.generated.resources.saveAsTemplate
import noteapp.shared.core.design_system.generated.resources.scan
import noteapp.shared.core.design_system.generated.resources.screen_protection
import noteapp.shared.core.design_system.generated.resources.screen_protection_detail
import noteapp.shared.core.design_system.generated.resources.search
import noteapp.shared.core.design_system.generated.resources.search_history
import noteapp.shared.core.design_system.generated.resources.security
import noteapp.shared.core.design_system.generated.resources.select_audio
import noteapp.shared.core.design_system.generated.resources.settings
import noteapp.shared.core.design_system.generated.resources.share
import noteapp.shared.core.design_system.generated.resources.shareContent
import noteapp.shared.core.design_system.generated.resources.share_note_as
import noteapp.shared.core.design_system.generated.resources.share_this_app
import noteapp.shared.core.design_system.generated.resources.small
import noteapp.shared.core.design_system.generated.resources.sort_by
import noteapp.shared.core.design_system.generated.resources.source_code
import noteapp.shared.core.design_system.generated.resources.sponsor
import noteapp.shared.core.design_system.generated.resources.standard_mode
import noteapp.shared.core.design_system.generated.resources.storage
import noteapp.shared.core.design_system.generated.resources.storage_detail
import noteapp.shared.core.design_system.generated.resources.strikethrough
import noteapp.shared.core.design_system.generated.resources.style
import noteapp.shared.core.design_system.generated.resources.sync
import noteapp.shared.core.design_system.generated.resources.system_default
import noteapp.shared.core.design_system.generated.resources.table
import noteapp.shared.core.design_system.generated.resources.task
import noteapp.shared.core.design_system.generated.resources.task_list
import noteapp.shared.core.design_system.generated.resources.template_folder_location
import noteapp.shared.core.design_system.generated.resources.templates
import noteapp.shared.core.design_system.generated.resources.text
import noteapp.shared.core.design_system.generated.resources.text_overflow
import noteapp.shared.core.design_system.generated.resources.the_feature_is_still_under_construction
import noteapp.shared.core.design_system.generated.resources.time_format
import noteapp.shared.core.design_system.generated.resources.time_in_the_template_file_will_be_replaced_with_this_value
import noteapp.shared.core.design_system.generated.resources.tip_alert
import noteapp.shared.core.design_system.generated.resources.title
import noteapp.shared.core.design_system.generated.resources.title_align
import noteapp.shared.core.design_system.generated.resources.transparent
import noteapp.shared.core.design_system.generated.resources.trash
import noteapp.shared.core.design_system.generated.resources.underline
import noteapp.shared.core.design_system.generated.resources.undo
import noteapp.shared.core.design_system.generated.resources.unlock_to_use_open_note
import noteapp.shared.core.design_system.generated.resources.uri_example
import noteapp.shared.core.design_system.generated.resources.url
import noteapp.shared.core.design_system.generated.resources.username
import noteapp.shared.core.design_system.generated.resources.version
import noteapp.shared.core.design_system.generated.resources.video
import noteapp.shared.core.design_system.generated.resources.view_all_notes
import noteapp.shared.core.design_system.generated.resources.warning
import noteapp.shared.core.design_system.generated.resources.warning_alert
import noteapp.shared.core.design_system.generated.resources.web_url
import noteapp.shared.core.design_system.generated.resources.weekly
import noteapp.shared.core.design_system.generated.resources.widget
import noteapp.shared.core.design_system.generated.resources.word_count
import noteapp.shared.core.design_system.generated.resources.word_count_without_punctuation
import noteapp.shared.core.design_system.generated.resources.you_can_also_use_date_yyyy_mm_dd_to_override_the_format_once
import noteapp.shared.core.design_system.generated.resources.you_can_also_use_time_hh_mm_to_override_the_format_once
import noteapp.shared.core.design_system.generated.resources.your_current_syntax_looks_like_this
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.StringResource

object NoteAppDrawables {
    val apparel24px: DrawableResource = Res.drawable.apparel_24px
    val icMenuShare: DrawableResource = Res.drawable.ic_menu_share
    val icMenuCopy: DrawableResource = Res.drawable.ic_menu_copy
    val icMenuDelete: DrawableResource = Res.drawable.ic_menu_delete
    val icMenuEdit: DrawableResource = Res.drawable.ic_menu_edit
    val markdownCopy: DrawableResource = Res.drawable.markdown_copy
    val noResults: DrawableResource = Res.drawable.no_results
    val noResultsLight: DrawableResource = Res.drawable.no_results_light
    val noConversation: DrawableResource = Res.drawable.no_conversation
    val noConversationLight: DrawableResource = Res.drawable.no_conversation_light
    val noNotes: DrawableResource = Res.drawable.no_notes
    val noTask: DrawableResource = Res.drawable.no_task
    val noTaskLight: DrawableResource = Res.drawable.no_task_light
    val onboard1: DrawableResource = Res.drawable.onboard1
    val onboard2: DrawableResource = Res.drawable.onboard2
    val onboard3: DrawableResource = Res.drawable.onboard3
    val composeMultiplatform: DrawableResource = Res.drawable.compose_multiplatform
}

object NoteAppFonts {
    val geomanistMedium: FontResource = Res.font.geomanist_medium
    val geomanistRegular: FontResource = Res.font.geomanist_regular
    val montserratBold: FontResource = Res.font.montserratbold
}

object NoteAppStrings {
    val title: StringResource = Res.string.title
    val search: StringResource = Res.string.search
    val allNotes: StringResource = Res.string.all_notes
    val trash: StringResource = Res.string.trash
    val folders: StringResource = Res.string.folders
    val manageFolders: StringResource = Res.string.manage_folders
    val settings: StringResource = Res.string.settings
    val style: StringResource = Res.string.style
    val darkMode: StringResource = Res.string.dark_mode
    val colorPlatte: StringResource = Res.string.color_platte
    val language: StringResource = Res.string.language
    val password: StringResource = Res.string.password
    val privacyPolicy: StringResource = Res.string.privacy_policy
    val rateThisApp: StringResource = Res.string.rate_this_app
    val shareThisApp: StringResource = Res.string.share_this_app
    val systemDefault: StringResource = Res.string.system_default
    val light: StringResource = Res.string.light
    val dark: StringResource = Res.string.dark
    val dynamicOnlyAndroid12: StringResource = Res.string.dynamic_only_android_12
    val edited: StringResource = Res.string.edited
    val searchHistory: StringResource = Res.string.search_history
    val content: StringResource = Res.string.content
    val delete: StringResource = Res.string.delete
    val resetDatabase: StringResource = Res.string.reset_database
    val shareContent: StringResource = Res.string.shareContent
    val remind: StringResource = Res.string.remind
    val restoreAll: StringResource = Res.string.restore_all
    val deleteAll: StringResource = Res.string.delete_all
    val move: StringResource = Res.string.move
    val checked: StringResource = Res.string.checked
    val modify: StringResource = Res.string.modify
    val warning: StringResource = Res.string.warning
    val deletingAFolderWillAlsoDeleteAllTheNotesItContainsAndTheyCannotBeRestoredDoYouWantToContinue: StringResource = Res.string.deleting_a_folder_will_also_delete_all_the_notes_it_contains_and_they_cannot_be_restored_do_you_want_to_continue
    val restore: StringResource = Res.string.restore
    val appInfo: StringResource = Res.string.app_info
    val version: StringResource = Res.string.version
    val export: StringResource = Res.string.export
    val share: StringResource = Res.string.share
    val name: StringResource = Res.string.name
    val uriExample: StringResource = Res.string.uri_example
    val link: StringResource = Res.string.link
    val reset: StringResource = Res.string.reset
    val resetDatabaseWarning: StringResource = Res.string.reset_database_warning
    val task: StringResource = Res.string.task
    val liteMode: StringResource = Res.string.lite_mode
    val guide: StringResource = Res.string.guide
    val noPasswordSet: StringResource = Res.string.no_password_set
    val noCalendarAppFound: StringResource = Res.string.no_calendar_app_found
    val progress: StringResource = Res.string.progress
    val importFiles: StringResource = Res.string.import_files
    val destinationFolder: StringResource = Res.string.destination_folder
    val languageDescription: StringResource = Res.string.language_description
    val data: StringResource = Res.string.data
    val security: StringResource = Res.string.security
    val importFilesDescription: StringResource = Res.string.import_files_description
    val backupDescription: StringResource = Res.string.backup_description
    val restoreDescription: StringResource = Res.string.restore_description
    val resetDatabaseDescription: StringResource = Res.string.reset_database_description
    val backup: StringResource = Res.string.backup
    val recovery: StringResource = Res.string.recovery
    val passwordDescription: StringResource = Res.string.password_description
    val date: StringResource = Res.string.date
    val ascending: StringResource = Res.string.ascending
    val descending: StringResource = Res.string.descending
    val sortBy: StringResource = Res.string.sort_by
    val table: StringResource = Res.string.table
    val row: StringResource = Res.string.row
    val column: StringResource = Res.string.column
    val viewAllNotes: StringResource = Res.string.view_all_notes
    val incorrectLinkFormat: StringResource = Res.string.incorrect_link_format
    val webUrl: StringResource = Res.string.web_url
    val shareNoteAs: StringResource = Res.string.share_note_as
    val file: StringResource = Res.string.file
    val text: StringResource = Res.string.text
    val exportAs: StringResource = Res.string.export_as
    val noteEditing: StringResource = Res.string.note_editing
    val undo: StringResource = Res.string.undo
    val redo: StringResource = Res.string.redo
    val bold: StringResource = Res.string.bold
    val italic: StringResource = Res.string.italic
    val underline: StringResource = Res.string.underline
    val strikethrough: StringResource = Res.string.strikethrough
    val mark: StringResource = Res.string.mark
    val scan: StringResource = Res.string.scan
    val preview: StringResource = Res.string.preview
    val code: StringResource = Res.string.code
    val quote: StringResource = Res.string.quote
    val alert: StringResource = Res.string.alert
    val math: StringResource = Res.string.math
    val horizontalRule: StringResource = Res.string.horizontal_rule
    val taskList: StringResource = Res.string.task_list
    val mermaidDiagram: StringResource = Res.string.mermaid_diagram
    val unlockToUseOpenNote: StringResource = Res.string.unlock_to_use_open_note
    val cloud: StringResource = Res.string.cloud
    val sync: StringResource = Res.string.sync
    val theFeatureIsStillUnderConstruction: StringResource = Res.string.the_feature_is_still_under_construction
    val url: StringResource = Res.string.url
    val username: StringResource = Res.string.username
    val pass: StringResource = Res.string.pass
    val colorPicker: StringResource = Res.string.color_picker
    val navigateBack: StringResource = Res.string.navigate_back
    val mode: StringResource = Res.string.mode
    val standardMode: StringResource = Res.string.standard_mode
    val editor: StringResource = Res.string.editor
    val editorDescription: StringResource = Res.string.editor_description
    val defaultViewForNote: StringResource = Res.string.default_view_for_note
    val defaultView: StringResource = Res.string.default_view
    val defaultEditingMode: StringResource = Res.string.default_editing_mode
    val defaultEditingModeForNote: StringResource = Res.string.default_editing_mode_for_note
    val editingView: StringResource = Res.string.editing_view
    val readingView: StringResource = Res.string.reading_view
    val sponsor: StringResource = Res.string.sponsor
    val note: StringResource = Res.string.note
    val notes: StringResource = Res.string.notes
    val category: StringResource = Res.string.category
    val categories: StringResource = Res.string.categories
    val image: StringResource = Res.string.image
    val list: StringResource = Res.string.list
    val find: StringResource = Res.string.find
    val replace: StringResource = Res.string.replace
    val lint: StringResource = Res.string.lint
    val lintDescription: StringResource = Res.string.lint_description
    val compose: StringResource = Res.string.compose
    val storage: StringResource = Res.string.storage
    val storageDetail: StringResource = Res.string.storage_detail
    val openFileManager: StringResource = Res.string.open_file_manager
    val rootDirectoryLocation: StringResource = Res.string.root_directory_location
    val currentRootDirectoryLocationOfTheFileRepository: StringResource = Res.string.current_root_directory_location_of_the_file_repository
    val change: StringResource = Res.string.change
    val print: StringResource = Res.string.print
    val templates: StringResource = Res.string.templates
    val dateFormat: StringResource = Res.string.date_format
    val timeFormat: StringResource = Res.string.time_format
    val dateInTheTemplateFileWillBeReplacedWithThisValue: StringResource = Res.string.date_in_the_template_file_will_be_replaced_with_this_value
    val youCanAlsoUseDateYyyyMmDdToOverrideTheFormatOnce: StringResource = Res.string.you_can_also_use_date_yyyy_mm_dd_to_override_the_format_once
    val forMoreSyntaxReferTo: StringResource = Res.string.for_more_syntax_refer_to
    val formatReference: StringResource = Res.string.format_reference
    val yourCurrentSyntaxLooksLikeThis: StringResource = Res.string.your_current_syntax_looks_like_this
    val timeInTheTemplateFileWillBeReplacedWithThisValue: StringResource = Res.string.time_in_the_template_file_will_be_replaced_with_this_value
    val youCanAlsoUseTimeHhMmToOverrideTheFormatOnce: StringResource = Res.string.you_can_also_use_time_hh_mm_to_override_the_format_once
    val templateFolderLocation: StringResource = Res.string.template_folder_location
    val filesInThisFolderWillBeAvailableAsTemplates: StringResource = Res.string.files_in_this_folder_will_be_available_as_templates
    val saveAsTemplate: StringResource = Res.string.saveAsTemplate
    val screenProtection: StringResource = Res.string.screen_protection
    val screenProtectionDetail: StringResource = Res.string.screen_protection_detail
    val fontSize: StringResource = Res.string.font_size
    val fontSizeDetail: StringResource = Res.string.font_size_detail
    val sourceCode: StringResource = Res.string.source_code
    val audio: StringResource = Res.string.audio
    val selectAudio: StringResource = Res.string.select_audio
    val recordAudio: StringResource = Res.string.record_audio
    val video: StringResource = Res.string.video
    val reportABugOrRequestAFeature: StringResource = Res.string.report_a_bug_or_request_a_feature
    val frequencyBackup: StringResource = Res.string.frequency_backup
    val never: StringResource = Res.string.never
    val daily: StringResource = Res.string.daily
    val weekly: StringResource = Res.string.weekly
    val monthly: StringResource = Res.string.monthly
    val biometric: StringResource = Res.string.biometric
    val createPassword: StringResource = Res.string.create_password
    val enterAgain: StringResource = Res.string.enter_again
    val clean: StringResource = Res.string.clean
    val freeUpSpace: StringResource = Res.string.free_up_space
    val freeUpDetail: StringResource = Res.string.free_up_detail
    val textOverflow: StringResource = Res.string.text_overflow
    val cardSize: StringResource = Res.string.card_size
    val ellipsis: StringResource = Res.string.ellipsis
    val clip: StringResource = Res.string.clip
    val defaultSize: StringResource = Res.string.default_size
    val compact: StringResource = Res.string.compact
    val flat: StringResource = Res.string.flat
    val overview: StringResource = Res.string.overview
    val charCount: StringResource = Res.string.char_count
    val wordCount: StringResource = Res.string.word_count
    val wordCountWithoutPunctuation: StringResource = Res.string.word_count_without_punctuation
    val lineCount: StringResource = Res.string.line_count
    val paragraphCount: StringResource = Res.string.paragraph_count
    val outline: StringResource = Res.string.outline
    val autoSave: StringResource = Res.string.auto_save
    val askSaveNote: StringResource = Res.string.ask_save_note
    val titleAlign: StringResource = Res.string.title_align
    val left: StringResource = Res.string.left
    val center: StringResource = Res.string.center
    val right: StringResource = Res.string.right
    val exploreAndShareCommunityNoteTemplates: StringResource = Res.string.explore_and_share_community_note_templates
    val widget: StringResource = Res.string.widget
    val color: StringResource = Res.string.color
    val small: StringResource = Res.string.small
    val medium: StringResource = Res.string.medium
    val large: StringResource = Res.string.large
    val lines: StringResource = Res.string.lines
    val transparent: StringResource = Res.string.transparent
    val createNote: StringResource = Res.string.create_note
    val editTextFile: StringResource = Res.string.edit_text_file
    val createTextFile: StringResource = Res.string.create_text_file
    val raw: StringResource = Res.string.raw
    val displayStyle: StringResource = Res.string.display_style
    val lineNumbers: StringResource = Res.string.line_numbers
    val sampleNote: StringResource = Res.string.sample_note
    val cautionAlert: StringResource = Res.string.caution_alert
    val warningAlert: StringResource = Res.string.warning_alert
    val importantAlert: StringResource = Res.string.important_alert
    val tipAlert: StringResource = Res.string.tip_alert
    val noteAlert: StringResource = Res.string.note_alert
    val cancel: StringResource = Res.string.cancel
    val ok: StringResource = Res.string.ok
}