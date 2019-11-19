package com.aceman.soireegaming.ui.profile

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.bottomnavigation.messages.EditProfileContract
import com.aceman.soireegaming.ui.bottomnavigation.messages.EditProfilePresenter
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity(override val activityLayout: Int = R.layout.activity_edit_profile) : BaseActivity(), BaseView, EditProfileContract.EditProfileViewInterface {
    private val mPresenter: EditProfilePresenter = EditProfilePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(edit_profile_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mPresenter.attachView(this)

        deleteCheckBox()
    }

    private fun deleteCheckBox() {
        edit_profile_checkbox.setOnClickListener {
            if(!edit_profile_checkbox.isChecked)
                edit_profile_delete_btn.setBackgroundResource(R.color.greyTextColor)
            else
                edit_profile_delete_btn.setBackgroundResource(R.color.errorTextColor)}

            edit_profile_delete_btn.setOnClickListener {
                if(!edit_profile_checkbox.isChecked)
                Toast.makeText(this,"Il faut cocher",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_profile_menu_tb, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.edit_profile_tb_validate -> {
                //TODO
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
