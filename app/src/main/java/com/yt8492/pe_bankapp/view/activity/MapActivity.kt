package com.yt8492.pe_bankapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.yt8492.pe_bankapp.R
import com.yt8492.pe_bankapp.view.fragment.MapFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import javax.inject.Named

class MapActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @JvmField
    @field:[Inject Named("Columns")]
    var columns = 0

    @JvmField
    @field:[Inject Named("Rows")]
    var rows = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        AndroidInjection.inject(this)

        val mapFragment = MapFragment.newInstance(columns, rows)
        supportFragmentManager.commit {
            add(R.id.fragmentContainer, mapFragment)
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
