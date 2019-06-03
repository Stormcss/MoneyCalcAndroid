package ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic

import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import java.io.IOException
import java.util.regex.Matcher
import java.util.regex.Pattern

class ComponentsUtils {

    companion object {
        private const val messageRegex = "\"userMessage\":\"(.*?)\""
        private const val messageHtmlRegex = "<title>(.*?)</title>"
        private val messageGetterPattern = Pattern.compile(messageRegex)
        private val messageHtmlGetterPattern = Pattern.compile(messageHtmlRegex)

        fun getSpendingSectionByInnerId(spendingSections: List<SpendingSection>?, id: Int): SpendingSection? {
            if (spendingSections == null)
                return null
            for (spendingSection in spendingSections) {
                if (spendingSection.sectionId == id)
                    return spendingSection
            }
            return null
        }

        fun getPositionBySpendingSectionInnerId(spendingSections: List<SpendingSection>?, sectionInnerId: Int): Int {
            if (spendingSections != null) {
                for (i in spendingSections.indices) {
                    if (spendingSections[i].sectionId == sectionInnerId)
                        return i
                }
            }
            return 0
        }

        fun getSpendingSectionInnerIdByPosition(spendingSections: List<SpendingSection>?, position: Int): Int? {
            if (spendingSections != null) {
                if (spendingSections.size > position)
                    return spendingSections[position].sectionId
            }
            return null
        }

        fun getLogoIdBySectionId(spendingSections: List<SpendingSection>?, sectionId: Int?): Int? {
            val spendingSection = getSpendingSectionByInnerId(spendingSections, sectionId!!)
            return spendingSection?.logoId
        }

        fun getLogoIdByName(spendingSections: List<SpendingSection>?, name: String?): Int? {
            return spendingSections
                    ?.filter { spendingSection -> spendingSection.name == name }
                    ?.map { spendingSection -> spendingSection.logoId }
                    ?.firstOrNull()
        }

        fun getStatsSummaryBySectionById(financeList: List<SummaryBySection>, id: Int): SummaryBySection? {
            for (financeSummaryBySection in financeList) {
                if (financeSummaryBySection.sectionId == id)
                    return financeSummaryBySection
            }
            return null
        }

        fun getErrorBodyMessage(t: Throwable): String {
            return if (t is HttpException)
                getErrorBodyMessage(t)
            else
                t.message ?: ""
        }

        fun getErrorBodyMessage(ex: HttpException): String {
            return try {
                val errorBody = ex.response().errorBody()
                val errorJSON = errorBody!!.string()

                System.err.println("errorJSON = $errorJSON")
                val messageMatcher: Matcher?

                if (errorBody.contentType()?.type().equals("text")) {
                    messageMatcher = messageHtmlGetterPattern.matcher(errorJSON)
                } else {
                    messageMatcher = messageGetterPattern.matcher(errorJSON)
                }

                if (messageMatcher.find()) {
                    messageMatcher.group(1)
                } else {
                    errorJSON
                }
            } catch (e1: IOException) {
                e1.printStackTrace()
                ex.message ?: ""
            }
        }
    }
}
