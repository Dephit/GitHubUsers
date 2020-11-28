package com.example.githubuserlistapp

import android.os.Parcel
import android.os.Parcelable

data class User(
    var avatar_url: String = "",
    var bio: String = "",
    var blog: String = "",
    var company: String = "",
    var created_at: String = "",
    var email: String = "",
    var events_url: String = "",
    var followers: Int = 0,
    var followers_url: String = "",
    var following: Int = 0,
    var following_url: String = "",
    var gists_url: String = "",
    var gravatar_id: String = "",
    var hireable: Boolean = false,
    var html_url: String = "",
    var id: Int = 0,
    var location: String = "",
    var login: String = "",
    var name: String = "",
    var node_id: String = "",
    var organizations_url: String = "",
    var public_gists: Int = 0,
    var public_repos: Int = 0,
    var received_events_url: String = "",
    var repos_url: String = "",
    var site_admin: Boolean = false,
    var starred_url: String = "",
    var subscriptions_url: String = "",
    var twitter_username: String = "",
    var type: String = "",
    var updated_at: String = "",
    var url: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(avatar_url)
        parcel.writeString(bio)
        parcel.writeString(blog)
        parcel.writeString(company)
        parcel.writeString(created_at)
        parcel.writeString(email)
        parcel.writeString(events_url)
        parcel.writeInt(followers)
        parcel.writeString(followers_url)
        parcel.writeInt(following)
        parcel.writeString(following_url)
        parcel.writeString(gists_url)
        parcel.writeString(gravatar_id)
        parcel.writeByte(if (hireable) 1 else 0)
        parcel.writeString(html_url)
        parcel.writeInt(id)
        parcel.writeString(location)
        parcel.writeString(login)
        parcel.writeString(name)
        parcel.writeString(node_id)
        parcel.writeString(organizations_url)
        parcel.writeInt(public_gists)
        parcel.writeInt(public_repos)
        parcel.writeString(received_events_url)
        parcel.writeString(repos_url)
        parcel.writeByte(if (site_admin) 1 else 0)
        parcel.writeString(starred_url)
        parcel.writeString(subscriptions_url)
        parcel.writeString(twitter_username)
        parcel.writeString(type)
        parcel.writeString(updated_at)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}