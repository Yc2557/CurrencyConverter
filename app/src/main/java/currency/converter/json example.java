
GSON

{
    "MAIN" : "AUD"

    "TO" : "USD" : [{
        "RATE":"10",
        "DATE":"09/10/22"
    }, {
        "RATE":"9.8", 
        "DATE":"10/10/22"
    }, {
        "":""
    }]
    "TO" : "NZD" : [
        {

        }
    ]
}

{

    String main

    String to [ (float rate, String date),(rate,date) ]
    String to [ (rate,date),(rate,date) ]


}

cur1, cur2, startdate, enddate
find cur1
    find cur2
        if date[i] > startdate: 
            return 

