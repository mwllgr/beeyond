Feature:
    Background:
        * url baseUrl
        * path 'template'
        * print teacherAuth
        * configure headers = { Authorization: '#(teacherAuth())'}
        * print headers

    Scenario: Create a valid template application
        * def content = read('nginx-deployment-template.yml.txt')
        Given request
        """
        {
          "name": "Nginx Deployment",
          "description": "Static Webserver",
          "content": "#(content)",
          "fields": [
            {
              "label": "Server count",
              "wildcard": "replica",
              "description": "How many server should there be?"
            },
            {
              "label": "Port of your saver",
              "wildcard": "port",
              "description": "This will be the port that will be exposed to the world"
            }
          ]
        }
        """
        When method POST
        Then status 204
        * print 'headers:', karate.prevRequest.headers
