package com.boka2.gesblue.Boka2ols

import com.boka2.gesblue.R
import com.boka2.gesblue.datamanager.database.model.Model_Denuncia
import com.boka2.gesblue.model.Model_Group_Denuncies
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Kotlin_Utils {
    companion object {
        public fun Denuncies_Filter(
            llistat_denuncies: List<Model_Denuncia>,
            filtre_matricula: String?
        ): List<Model_Group_Denuncies> {
            val result: MutableList<Model_Group_Denuncies> = mutableListOf()
            val simpleDate = SimpleDateFormat("dd/MM/yyyy")
            val llista = llistat_denuncies.toMutableList()
            if (llista.size > 0) {
                var excludes = Model_Group_Denuncies()
                excludes.date = "#ERROR DATA"
                val guia = JSONObject()
                for (x in llista.indices) {

                   fun Add(){
                       val data: Date? = llista[x].fechacreacio
                       if (data != null) {
                           if (guia.has(simpleDate.format(data))) {
                               try {
                                   val index =guia.getInt(simpleDate.format(data)) //GET POSITION STORED IN JSONOBJECT
                                   val temp: Model_Group_Denuncies = result[index]
                                   temp.llistat.add(llista[x])

                                   when(llista[x].tipusanulacio.toString()) {
                                       "-1.0"->temp.pendents_imprimir++
                                       "0.0"->temp.pendents_enviar++
                                       "1.0"->temp.enviades++
                                       "-2.0"->temp.fallides++
                                   }

                                   result[index] = temp
                               } catch (e: JSONException) {
                                   e.printStackTrace()
                               }
                           }
                           else {
                               val group = Model_Group_Denuncies()
                               val temp: MutableList<Model_Denuncia> = mutableListOf()
                               val element: Model_Denuncia = llista[x]
                               temp.add(element)
                               group.llistat = temp
                               group.date = simpleDate.format(data) //SET DATE

                               when(element.tipusanulacio.toString()) {
                                   "-1.0"->group.pendents_imprimir++
                                   "0.0"->group.pendents_enviar++
                                   "1.0"->group.enviades++
                                   "-2.0"->group.fallides++
                               }

                               result.add(group)
                               try {
                                   guia.put(simpleDate.format(data), result.size-1)
                               } catch (e: JSONException) {
                                   e.printStackTrace()
                               }
                           }
                       }
                       else {

                           when(llista[x].tipusanulacio.toString()) {
                               "-1.0"->excludes.pendents_imprimir++
                               "0.0"->excludes.pendents_enviar++
                               "1.0"->excludes.enviades++
                               "-2.0"->excludes.fallides++
                           }
                           if(excludes.llistat.size>0) {
                               val element: Model_Denuncia = llista[x]
                               var temp =excludes.llistat
                               temp.add(element)
                               excludes.llistat=temp
                           }
                           else{
                               val element: Model_Denuncia = llista[x]
                               var temp = mutableListOf<Model_Denuncia>()
                               temp.add(element)
                               excludes.llistat=temp
                           }
                       }
                   }

                    if(filtre_matricula==null || filtre_matricula==""){
                        Add()
                    }
                    else{
                        if(llista[x].matricula.startsWith(filtre_matricula)){
                            Add()
                        }
                    }


                }
                if(excludes.llistat.size>0) {
                    result.add(excludes)
                }
                // specify an adapter (see also next example)
            }
            return result.toList()
        }



    }
}