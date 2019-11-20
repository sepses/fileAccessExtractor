var stream = require('stream');
var util = require('util');
var WebSocket = require('ws')

// node v0.10+ use native Transform, else polyfill
var Transform = stream.Transform ||
  require('readable-stream').Transform;

function LogstashStream(options) {
  // allow use without new
  if (!(this instanceof LogstashStream)) {
    return new LogstashStream(options);
  }
  
	// Create a new WebSocket.
	this.socket = new WebSocket('ws://0.0.0.0:3232/');
	var _this = this;
	
	this.socket.on("message", function incoming(data) {
		console.log(JSON.parse(data));
		if (!_this.close) {
      		_this.push(JSON.parse(data));
      	} else {
     	 	_this.push(null);
   		}
	});
	

  // init Transform
  Transform.call(this, options);
}
util.inherits(LogstashStream, Transform);

LogstashStream.prototype._read = function(enc, cb) {};

LogstashStream.prototype.closeStream = function() {
  this.close = true;
};

exports = module.exports = LogstashStream;