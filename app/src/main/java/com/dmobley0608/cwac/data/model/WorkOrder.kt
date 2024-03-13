package com.dmobley0608.cwac.data.model

import androidx.compose.ui.graphics.Color
import com.dmobley0608.cwac.ui.theme.PriorityHigh
import com.dmobley0608.cwac.ui.theme.PriorityLow
import com.dmobley0608.cwac.ui.theme.PriorityModerate

//Priority level
open class Priority(val name:String?="", val level:Int?=1, val color: Color = PriorityLow){
    constructor():this("", 1, PriorityLow)
    object Low:Priority("Low", 1, PriorityLow)
    object Moderate:Priority("Moderate", 2, PriorityModerate)
    object High:Priority("High", 3, PriorityHigh)
}


data class WorkOrder(
    var id:String? = "",
    var title: String? = "",
    var priority: Int? = Priority.Low.level,
    var complete: Boolean? =false,
    var description:String?="",
    var assignedTo:User?=User(),
    val date: Long? = System.currentTimeMillis(),
    var createdBy:String?="",
    var completedBy:User?=User(),
    var safeDeleted:Boolean?=false
)

