package com.lpennavic.generalboard.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lpennavic.generalboard.Adapter.ProfilePosterAdapter
import com.lpennavic.generalboard.DAO.PosterDAO
import com.lpennavic.generalboard.DAO.UserDAO
import com.lpennavic.generalboard.NicknameChangeBox
import com.lpennavic.generalboard.R

class ProfileFragment(val userId: String): Fragment(R.layout.fragment_profile) {

    lateinit var launcher: ActivityResultLauncher<Intent>
    lateinit var bitmap: Bitmap
    lateinit var profileImgProfile: ImageView
    lateinit var profileTextNickname: TextView
    lateinit var profileTextUserId: TextView
    lateinit var profileTextIntro: TextView
    lateinit var profileEditIntro: EditText
    lateinit var profileListPosterlist: RecyclerView
    lateinit var profileBtnIntro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
            result.data?.data?.let {
                uri ->
                val option = BitmapFactory.Options()
                option.inSampleSize = 5
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream, null, option)!!
                inputStream?.close()

                val user = UserDAO.selectUser(requireContext(), userId)
                if(user !=null) {
                    UserDAO.updateUser(requireContext(), 3, bitmap, user)
                    profileImgProfile.setImageBitmap(bitmap)
                }

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileImgProfile = view.findViewById(R.id.profile_img_profile)
        profileTextNickname = view.findViewById(R.id.profile_text_nickname)
        profileTextUserId = view.findViewById(R.id.profile_text_userid)
        profileTextIntro = view.findViewById(R.id.profile_text_intro)
        profileEditIntro = view.findViewById(R.id.profile_edit_intro)
        profileListPosterlist = view.findViewById(R.id.profile_list_posterlist)
        profileBtnIntro = view.findViewById(R.id.profile_btn_intro)

        // bitmap = BitmapFactory.decodeResource(view.context.resources, R.drawable.profile_5050)

        val user = UserDAO.selectUser(view.context, userId)
        if(user != null) {
            val userProfileFile = user.profile?.absolutePath
            val profileBitmap = BitmapFactory.decodeFile(userProfileFile)
            profileImgProfile.setImageBitmap(profileBitmap)
            profileTextNickname.text = user.nickname
            val userIdStr = """@${user.id}"""
            profileTextUserId.text = userIdStr
            profileTextIntro.text = user.intro

            profileImgProfile.setOnClickListener {
                // 프로필 이미지 변경
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                launcher.launch(intent)
            }

            profileTextNickname.setOnClickListener {
                // 닉네임 변경
                val intent = Intent(view.context, NicknameChangeBox::class.java)
                intent.putExtra("user", userId)
                startActivity(intent)
                activity?.finish()
            }

            // 자기소개 변경
            profileTextIntro.setOnClickListener {
                profileTextIntro.visibility = View.GONE
                profileEditIntro.visibility = View.VISIBLE
                profileBtnIntro.visibility = View.VISIBLE
                profileEditIntro.requestFocus()
                profileEditIntro.setText(profileTextIntro.text)
            }
            // 에딧텍스트 길이 만큼 커서 위치를 설정
            profileEditIntro.setOnFocusChangeListener { view, hasFocus ->
                if(hasFocus) profileEditIntro.post { profileEditIntro.setSelection(profileEditIntro.text.length) }
            }
            profileBtnIntro.setOnClickListener {
                val introText = profileEditIntro.text.toString()
                profileTextIntro.text = introText
                profileEditIntro.visibility = View.GONE
                profileTextIntro.visibility = View.VISIBLE
                profileEditIntro.clearFocus()
                it.visibility = View.GONE
                UserDAO.updateUser(view.context, 4, introText, user)

                // 수정 버튼이 눌리면 소프트 키보드 숨기기
                val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(profileEditIntro.windowToken, 0)
            }

            // 현재 로그인 중인 사용자가 작성한 poster 리사이클러뷰
            val posters = PosterDAO.selectPosterByWriter(view.context, userId)
            val profilePosterAdapter = ProfilePosterAdapter(view.context, userId, posters)
            profileListPosterlist.layoutManager = LinearLayoutManager(view.context)
            profileListPosterlist.adapter = profilePosterAdapter
        }


    }


}