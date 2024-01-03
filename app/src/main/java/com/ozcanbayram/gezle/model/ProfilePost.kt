data class ProfilePost(
    val id: String, // Eğer belge oluşturulduktan sonra bu alanı alabiliyorsanız.
    val downloadUrl: String,
    val email: String,
    val ad_soyad: String,
    val comment: String,
    val latitudeInfo: String,
    val longitudeInfo: String,
    val place_name: String
)
