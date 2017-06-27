package com.couchgram.gamebooster.ui.boostapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.couchgram.gamebooster.BaseMvpFragment
import com.couchgram.gamebooster.R
import com.couchgram.gamebooster.common.Constants
import com.couchgram.gamebooster.common.PERMS_GUIDE
import com.couchgram.gamebooster.data.source.BoostAppInfo
import com.couchgram.gamebooster.ui.ads.RewardvideoActivity
import com.couchgram.gamebooster.ui.boostapp.adapter.BoostAppAdapter
import com.couchgram.gamebooster.ui.boostapp.presenter.BoostAppContract
import com.couchgram.gamebooster.ui.boostapp.presenter.BoostAppPresenter
import com.couchgram.gamebooster.ui.guide.PermsGuide
import com.couchgram.gamebooster.ui.installedapp.InstalledAppListActivity
import com.couchgram.gamebooster.ui.setting.SettingActivity
import com.couchgram.gamebooster.ui.widget.listener.OnItemClickListener
import com.couchgram.gamebooster.ui.widget.dialog.ConfirmDialog
import com.couchgram.gamebooster.ui.widget.view.GridAutofitLayoutManager
import com.couchgram.gamebooster.util.ShortCutUtils
import com.couchgram.gamebooster.util.Utils
import com.mopub.mobileads.MoPubErrorCode
import com.mopub.mobileads.MoPubView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_boost_app_list.*


/**
 * Created by chonamdoo on 2017. 4. 28..
 */


class BoostAppListFragment : BaseMvpFragment<BoostAppContract.View,BoostAppContract.Presenter>() , BoostAppContract.View{


    private lateinit var adapter : BoostAppAdapter
    private lateinit var activity : BoostAppListActivity
    private var confirmDialog : ConfirmDialog?=null
    private var accessbilityDialog : ConfirmDialog?=null
    private var isLoadingAds = false

    private val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val permsGuide: PermsGuide by lazy {
        PermsGuide(activity,PERMS_GUIDE.ACCESSIBILITY_GUIDE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePresenter(): BoostAppContract.Presenter {
        return BoostAppPresenter(activity)
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_boost_app_list
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        recyclerview.layoutManager = GridAutofitLayoutManager(context,context.resources.getDimension(R.dimen.boost_layout_width ).toInt())
        recyclerview.setHasFixedSize(true)
        adapter = BoostAppAdapter(activity,object : OnItemClickListener{
            override fun onItemClick(item: Any, position: Int) {
                if(item is BoostAppInfo){
                    if(adapter.editMode){
                        presenter.removeBoostItem(item,position)
                    }else if(adapter.uninstallMode){
                        Intent(Intent.ACTION_DELETE).apply {
                            data = Uri.parse("package:${item.packageName}")
                            startActivity(this)
                        }
                    }else{
                        if(item.isAddItem){
                            activity.startActivity(Intent(activity,InstalledAppListActivity::class.java))
                        }else {
                            Intent(activity, RewardvideoActivity::class.java).apply {
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                putExtra(Constants.BOOST_ITEM, item)
                                startActivity(this)
                            }
                        }
                    }
                }
            }
        })
        recyclerview.adapter = adapter
        presenter.run {
            adapterModel = adapter
            adapterView = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
        updateLayout()
        checkAccessibility()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as BoostAppListActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adsView?.destroy()
    }
    override fun onDestroy() {
        compositeDisposable.dispose()
        accessbilityDialog?.dismiss()
        permsGuide.dismiss()
        super.onDestroy()
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_setting,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId){
                R.id.action_delete->{
                    adapter.updateEditMode(!adapter.editMode)
                }
                R.id.action_shortcut->{
                    confirmDialog?.dismiss()
                    confirmDialog = ConfirmDialog(activity).apply {
                        setNegativeClickListener(View.OnClickListener {
                            ShortCutUtils.deleteGameBoxActivityShortcut()
                            ShortCutUtils.createGameBoxActivityShortcut()
                        })
                        show()
                    }

                }
                R.id.action_uninstall-> {
                    adapter.updateUninstallMode(!adapter.uninstallMode)
                }
                R.id.action_setting->{
                    startActivity(Intent(activity,SettingActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    })
                }
                R.id.action_share->{
                    Utils.shareSns(activity)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isEditMode(): Boolean {
        return adapter.editMode or adapter.uninstallMode
    }

    override fun clearEditMode() {
        adapter.editMode = false
        adapter.uninstallMode = false
        adapter.notifyDataSetChanged()
    }
    fun initAdBanner(){
        isLoadingAds = true
        adsView.adUnitId = Constants.MOPUB_AD_ID
        adsView.bannerAdListener = object : MoPubView.BannerAdListener{
            override fun onBannerLoaded(banner: MoPubView) {
            }

            override fun onBannerFailed(banner: MoPubView, errorCode: MoPubErrorCode) {
            }

            override fun onBannerClicked(banner: MoPubView) {

            }

            override fun onBannerExpanded(banner: MoPubView) {

            }

            override fun onBannerCollapsed(banner: MoPubView) {

            }
        }
        adsView.loadAd()
    }
    fun checkInstallCouchGram() : Boolean{
        return Utils.isPackageInstalled(Constants.COUCHGRAM)
    }
    fun updateLayout(){
        if(checkInstallCouchGram() && !isLoadingAds){
            layout_install_couch.visibility = View.GONE
            adsView.visibility = View.VISIBLE
            guide_line.visibility = View.GONE
            initAdBanner()
        }else if(!checkInstallCouchGram()){
            isLoadingAds = false
            layout_install_couch.visibility = View.VISIBLE
            guide_line.visibility = View.VISIBLE
            layout_install_couch.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(Constants.MARKET_COUCH)
                    startActivity(this)
                }
            }
        }
    }
    fun checkAccessibility(){
        if(!Utils.checkEnabledAccessibilityService()){
            accessbilityDialog?.dismiss()
            accessbilityDialog = ConfirmDialog(activity).apply {
                setPositiveClickListener(View.OnClickListener {
                    Utils.reqAccessibilityPerms(activity)
                    permsGuide.show()
                    compositeDisposable.add(Utils.accessibilityPermissionChecker()
                            .subscribeOn(Schedulers.trampoline())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({},{},{
                                Utils.reqAccessibilityHomeIntent(activity)
                                startActivity(Intent(activity, BoostAppListActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                })
                            }))
                })
                setConfirmText(getString(R.string.setting))
                setTitleText(getString(R.string.noti))
                setContentText(getString(R.string.perms_boost))
                show()
            }
        }
    }
    companion object{
        fun newInstance() = BoostAppListFragment()

    }
}