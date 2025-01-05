"use strict";

/**
 * JavaScript object which is a plan with start data for the trial user
 */

function createTrialData() {

    const monday = {
        name: "Monday",
        tasks: [{ description: "Wake up", doneStatus: true },
            { description: "Drink a cup of Java ☕", doneStatus: true },
            { description: "Code Java", doneStatus: false }]
    };

    const tuesday = {
        name: "Tuesday",
        tasks: [{ description: "t is for tuesday", doneStatus: false }]
    };

    const wednesday = {
        name: "Wednesday",
        tasks: [{ description: "w is for wednesday", doneStatus: false }]
    };

    const thursday = {
        name: "Thursday",
        tasks: [{ description: "t is also for thursday", doneStatus: false }]
    };

    const friday = {
        name: "Friday",
        tasks: [{ description: "abcdefghijklmnopqrstuvxyzæøå", doneStatus: false }]
    };

    const saturday = {
        name: "Saturday",
        tasks: [{ description: "ABCDEFGHIJKLMNOPQRSTUVXYZÆØÅ", doneStatus: false }]
    };

    const sunday = {
        name: "Sunday",
        tasks: [{ description: "That&apos;s my fun day", doneStatus: false },
            { description: "My I don&apos;t have to run day", doneStatus: false }]
    };

    const week = {
        name: "Next Week",
        tasks: [{ description: "stuff to do next week", doneStatus: false }]
    };

    const month = {
        name: "Within a Month",
        tasks: [{ description: "Shower day", doneStatus: false }]
    };

    const year = {
        name: "Within a Year",
        tasks: [{ description: "stuff to do this year", doneStatus: false }]
    };

    const shopping = {
        name: "Shopping",
        tasks: [{ description: "Stuff to buy", doneStatus: false }]
    };


    const data = {
        lists: [monday, tuesday, wednesday, thursday, friday, saturday, sunday, week, month, year, shopping]
    };

    console.log(JSON.stringify(data));

    return data;

}