package fr.nicopico.blogengine.domain.entities

@JvmInline
value class Email(val address: String) {
    init {
        require(address.count { it == '@' } == 1) {
            "Address $address is invalid"
        }
    }
}
