@Grapes([
    @Grab(group='ru.yandex.qatools.allure', module='allure-bundle', version='1.4.22'),
    @Grab(group='javax.servlet', module='javax.servlet-api', version='3.0.1'),
    @Grab(group='org.eclipse.jetty.aggregate', module='jetty-all-server', version='8.1.8.v20121106', transitive=false)
])


import ru.yandex.qatools.allure.AllureMain
import java.util.concurrent.atomic.AtomicInteger
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.*
import javax.servlet.http.*
import javax.servlet.*
import groovy.servlet.*
import groovy.json.*

class Glob {
    static final REPORTDIR = '/reports'
    static BASEURL = 'http://localhost/'  // This one is filled at startup 
    static REPORTS = new AtomicInteger(0)
}

/**
    AllureGenerating servlet.
    POST a multipart data with report XML and attaches and stuff
    and receive answer with JSON of {result: OK, url: <url-for-report>}
*/
class ABServlet extends HttpServlet {
    void doPost(HttpServletRequest request, HttpServletResponse response) {
        println 'Receiving report data..'
        def report = Glob.REPORTS.incrementAndGet()
        
        def root = "/${Glob.REPORTDIR}/${report}/"
        def input = "${root}/input"
        def output = "${root}/output"
        new File(input).mkdirs()
        
        request.getParts().each{ part ->
            println "Writing ${part.name}"
            // write writes relatively to the Glob.REPORTDIR (as it is in MultipartCOnfigElement)
            // so we tailor the paths for it to work properly
            part.write("${report}/input/${part.name}")
        }
        
        println "Generating report ${report}.."
        AllureMain.main(input, output)
        println "Report ${report} done"
        
        response.writer.write(JsonOutput.toJson([result: 'OK', url: "${Glob.BASEURL}/${report}/output/index.html"]))
    }
}

def startJetty() {
    def jetty = new Server(80)

    def context = new ServletContextHandler(jetty, '/', ServletContextHandler.SESSIONS)  // Allow sessions.
    
    def generator = context.addServlet(ABServlet, '/generate')
    generator.getRegistration().setMultipartConfig(new MultipartConfigElement(Glob.REPORTDIR));
        
    def filesHolder = context.addServlet(DefaultServlet, '/')
    filesHolder.setInitParameter('resourceBase', Glob.REPORTDIR)

    jetty.start()
}

if (this.args[0] != null) {
    Glob.BASEURL = this.args[0]
}

println "Started service, base URL is ${Glob.BASEURL}, press Ctrl+C to stop."
startJetty()


