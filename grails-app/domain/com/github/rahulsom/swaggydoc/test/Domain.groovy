package com.github.rahulsom.swaggydoc.test

class Domain {

    String name

    static hasMany = [subdomains: Subdomain]
    static constraints = {
    }
}