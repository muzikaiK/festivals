import request from '@/utils/request';

const api = {
    getUpcomingEvents: '/api/calendar/upcoming',
    getRandomQuote: '/api/yiyan',
    getLifeProgress: '/api/life/progress',
    getSolarTerms: '/api/solar-terms/list',
    getHolidays: '/api/calendar/holidays',
};

export function getUpcomingEvents() {
    return request({
        url: api.getUpcomingEvents,
        method: 'GET'
    });
}

export function getRandomQuote() {
    return request({
        url: api.getRandomQuote,
        method: 'GET'
    });
}

export function getLifeProgress(params) {
    return request({
        url: api.getLifeProgress,
        method: 'GET',
        data: params
    });
}

export function getAllSolarTerms() {
    return request({
        url: api.getSolarTerms,
        method: 'GET'
    });
}

export function getHolidays() {
    return request({
        url: api.getHolidays,
        method: 'GET'
    });
}