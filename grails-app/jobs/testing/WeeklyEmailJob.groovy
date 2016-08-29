package testing

class WeeklyEmailJob {

    //13:00:00 every sunday  on sundayu pm on sundays this gets kicked off
    static triggers = {
        cron name: 'weeklyEmail', cronExpression:   "0  00 13 ? * SUN" //, timeZone: TimeZone.getTimeZone('BST')
                                                  // S  MM HH DOM ? DOW
    }
    def group=this.class.simpleName
    def schedulerService

    def execute() {
        log.info "Weekly schedule kicked off"
        schedulerService.weeklyEmail()
    }

}
