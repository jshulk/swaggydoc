package com.github.rahulsom.swaggydoc

import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
class ApiControllerSpec extends Specification {

    void "test resources"() {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.resources()

        then: "An expected json is returned"
        def json = controller.response.json
        json != null
        json.apiVersion == '1.0'
        json.swaggerVersion == '1.2'
        json.info.contact == 'rahul.som@gmail.com'
        json.apis.size() == 3
        json.apis.find {it.path == 'http://localhost/api/show/domain'}
        json.apis.find {it.path == 'http://localhost/api/show/domain'}.description == 'Domain Controller'
        json.apis.find {it.path == 'http://localhost/api/show/domainless'}
        json.apis.find {it.path == 'http://localhost/api/show/domainless'}.description == 'Domainless Controller'
    }

    void "test showing a controller with matching domain" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'domain'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        json.apiVersion == '1.0'
        json.swaggerVersion == '1.2'
        json.basePath == "http://localhost"
        json.resourcePath == "/domain/index"
        json.produces == ['application/json', 'application/xml', 'text/html']
        json.consumes == ['application/json', 'application/xml', 'application/x-www-form-urlencoded']
        json.apis.size() == 6

        json.apis.find {it.path == '/domain/index'}
        json.apis.find {it.path == '/domain/index'}.operations.size() == 1

        json.apis.find {it.path == '/domain/show/{id}'}
        json.apis.find {it.path == '/domain/show/{id}'}.operations.size() == 1

        json.apis.find {it.path == '/domain/delete/{id}'}
        json.apis.find {it.path == '/domain/delete/{id}'}.operations.size() == 1

        json.apis.find {it.path == '/domain/update/{id}'}
        json.apis.find {it.path == '/domain/update/{id}'}.operations.size() == 1

        json.apis.find {it.path == '/domain/patch/{id}'}
        json.apis.find {it.path == '/domain/patch/{id}'}.operations.size() == 1

        json.apis.find {it.path == '/domain/save'}
        json.apis.find {it.path == '/domain/save'}.operations.size() == 1

    }
    void "domains list required properties correctly" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'domain'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        json.models.size() == 2
        json.models.find {k,v -> k == 'Domain'}
        json.models.find {k,v -> k == 'Subdomain'}
        def domainModel = json.models['Domain']

        assert domainModel['id'] == 'Domain'
        def domainProps = domainModel['properties']
        assert domainModel['required'] as Set == ['name','id','version'] as Set
        assert domainProps.size() == 5

        assert domainProps.id
        assert domainProps.id.format == 'int64'
        assert domainProps.id.type == 'integer'

        assert domainProps.subdomains
        assert domainProps.subdomains.items['$ref'] == 'Subdomain'
        assert domainProps.subdomains.type == 'array'

        assert domainProps.description
        assert domainProps.description.type == 'string'

        assert domainProps.name
        assert domainProps.name.type == 'string'

        assert domainProps.version
        assert domainProps.version.type == 'integer'
        assert domainProps.version.format == 'int64'


    }

    void "test SwaggyList annotation with defaults" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'domain'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        def indexMethod = json.apis.find { it.path == '/domain/index' }
        indexMethod.operations.size() == 1
        indexMethod.operations[0].method == 'GET'
        indexMethod.operations[0].type == 'Domain'
        indexMethod.operations[0].parameters.size() == 5

    }

    void "test SwaggyList annotation without searchParam" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'domainless'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        def indexMethod = json.apis.find { it.path == '/domainless/index' }
        indexMethod.operations.size() == 1
        indexMethod.operations[0].method == 'GET'
        indexMethod.operations[0].type == 'Domainless'
        indexMethod.operations[0].parameters.size() == 4

    }

    void "test showing a controller with low level annotations" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'lowLevel'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        json.apiVersion == '1.0'
        json.swaggerVersion == '1.2'
        json.basePath == "http://localhost"
        json.resourcePath == "/lowLevel/index"
        json.produces == ['application/json', 'application/xml', 'text/html']
        json.consumes == ['application/json', 'application/xml', 'application/x-www-form-urlencoded']
        json.models.size() == 3
        json.apis.size() == 8

        json.models.find {k,v -> k == 'Domain'}
        json.models.find {k,v -> k == 'Void'}
        json.models.find {k,v -> k == 'Subdomain'}
        def demoModel = json.models.find {k,v -> k == 'Domain'}
    }

    void "test showing methods with params" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'lowLevel'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        json.apiVersion == '1.0'
        json.swaggerVersion == '1.2'
        json.basePath == "http://localhost"
        json.resourcePath == "/lowLevel/index"
        json.produces == ['application/json', 'application/xml', 'text/html']
        json.consumes == ['application/json', 'application/xml', 'application/x-www-form-urlencoded']
        json.models.size() == 3
        json.apis.size() == 8

        json.apis.find {it.path == '/lowLevel/duplicateMethod/{id}'}
        json.apis.find {it.path == '/lowLevel/duplicateMethod/{id}'}.operations.size() == 1

    }

    void "test showing a controller without matching domain" () {
        given: "A controller"
        def controller = new ApiController()

        when: "Resources are listed"
        controller.params.id = 'domainless'
        controller.show()

        then: "An expected json is returned"
        def json = controller.response.json
        json
        json.apiVersion == '1.0'
        json.swaggerVersion == '1.2'
        json.basePath == "http://localhost"
        json.resourcePath == "/domainless/index"
        json.produces == ['application/json', 'application/xml', 'text/html']
        json.consumes == ['application/json', 'application/xml', 'application/x-www-form-urlencoded']
        json.models.size() == 0
        json.apis.size() == 6
    }

}
