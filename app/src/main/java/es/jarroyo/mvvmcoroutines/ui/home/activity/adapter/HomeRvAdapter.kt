package es.jarroyo.mvvmcoroutines.ui.home.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jarroyo.mvvmcoroutines.R
import es.jarroyo.mvvmcoroutines.data.source.network.GithubAPI
import kotlinx.android.synthetic.main.item_rv_home.view.*

class HomeRvAdapter(private var mRepositorieList: List<GithubAPI.Repo>? = mutableListOf(),
                    private val listenerRepositorieClicked: (ItemRepositorie) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return mRepositorieList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_rv_home, parent, false)
        return AlarmViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val medication = mRepositorieList!!.get(position)
        val medicationHolder = holder as AlarmViewHolder
        medicationHolder.bind(medication, position, listenerRepositorieClicked)

    }

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(medication: GithubAPI.Repo, position: Int, listener: (ItemRepositorie) -> Unit) = with(itemView) {
            item_rv_home_tv_title.text = medication.name
            setOnClickListener {
                listener(ItemRepositorie(position, medication))
            }
        }
    }

    fun setMedicationList(medicationList: List<GithubAPI.Repo>){
        mRepositorieList = medicationList
    }

    fun getMedicationList(): List<GithubAPI.Repo>?{
       return  mRepositorieList
    }
}

data class ItemRepositorie(val position: Int, val repositorue: GithubAPI.Repo)