var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var responseTime = require('response-time');
var mkdirp = require('mkdirp');
var fs = require('fs');

var index = require('./routes/index');
var users = require('./routes/users');

var app = express();

function randomInt (low, high) {
    return Math.floor(Math.random() * (high - low) + low);
}

// setup the logger
app.use(responseTime(function (req, res, time) {
  var logDirName = "logs";
  mkdirp(logDirName);
  var accessLogStream = fs.createWriteStream(
      path.join(__dirname, logDirName, "/access." + new Date().getTime() + "." + randomInt(0, 1000000) + ".log"),
      {flags: 'a'});
  accessLogStream.write(time + "");
}))

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', index);
app.use('/users', users);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
