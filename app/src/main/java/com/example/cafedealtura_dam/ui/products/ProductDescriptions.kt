package com.example.cafedealtura_dam.ui.products

object ProductDescriptions {

    fun getDescription(name: String): String {

        return when (name) {

            "Costa Rica Tarrazú" ->
                "Cultivado en las altas montañas de Tarrazú, este café ofrece una acidez brillante con notas cítricas y un delicado fondo de chocolate."

            "Colombia Los Naranjos" ->
                "Un café colombiano lleno de dulzura natural con notas de caramelo y frutas maduras."

            "Etiopía Yrgacheff" ->
                "Café aromático con notas florales de jazmín y un toque cítrico brillante."

            else ->
                "Café de especialidad con perfil equilibrado y sabor único."

        }
    }
}