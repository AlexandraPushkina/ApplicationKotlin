package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        loadUserProfile()

        // TODO: Кнопка выхода (опционально, чтобы проверить логику)
    //        binding.btnLogout?.setOnClickListener {
    //            logout()
    //        }

    }

    private fun loadUserProfile() {
        // 1. Получаем сохраненный username (ключ должен совпадать с тем, что ты сохранял при Логине!)
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)


        // 2. Запускаем Корутину (асинхронный поток)
        // В Room все запросы должны идти не в главном потоке, но lifecycleScope
        // удобно переключает потоки за нас: запрос в фоне -> результат в UI.
        viewLifecycleOwner.lifecycleScope.launch {

            // Получаем доступ к DAO
            val database = AppDatabase.getDatabase(requireContext())
            val userDao = database.userDao()

            // 3. Делаем запрос (это suspend функция)
            val userProfile = userDao.getUserProfile(username as String)

            // 4. Проверяем результат и обновляем UI
            if (userProfile != null) {
                binding.tvUsername.text = userProfile.username
                binding.tvBio.text = userProfile.bio.ifEmpty { "Биография не заполнена" }
                binding.chipTopic.text = userProfile.topicName // Room сам подтянул название темы!
            } else {
                showError("Пользователь не найден в базе")
            }
        }
    }

    private fun showError(message: String) {
        binding.tvUsername.text = "Ошибка"
        binding.tvBio.text = message
        binding.chipTopic.text = "---"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}