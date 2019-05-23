package ru.strcss.projects.moneycalc.moneycalcandroid.utils.logic

import retrofit2.HttpException
import ru.strcss.projects.moneycalc.moneycalcdto.dto.crudcontainers.spendingsections.SpendingSectionsSearchRs
import ru.strcss.projects.moneycalc.moneycalcdto.entities.SpendingSection
import ru.strcss.projects.moneycalc.moneycalcdto.entities.statistics.SummaryBySection
import java.io.IOException
import java.util.regex.Pattern

class ComponentsUtils {

    companion object {
        private val messageRegex = "\"userMessage\":\"(.*?)\""
        private val messageGetterPattern = Pattern.compile(messageRegex)

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
            val s = SpendingSectionsSearchRs();

            val spendingSection = getSpendingSectionByInnerId(spendingSections, sectionId!!)
            return spendingSection?.logoId
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
                val errorJSON = ex.response().errorBody()!!.string()
                System.err.println("errorJSON = $errorJSON")
                val messageMatcher = messageGetterPattern.matcher(errorJSON)

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
