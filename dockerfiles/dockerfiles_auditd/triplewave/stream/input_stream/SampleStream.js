var stream = require('stream');
var util = require('util');

var Transform = stream.Transform || require('readable-stream').Transform;

function SampleStream(options) {

	WebSocket WebSocket(
  		in DOMString url,
  		in optional DOMString protocols
	);
	
	var exampleSocket = new WebSocket("ws://0.0.0.0", "3232");
	
	exampleSocket.onopen = function (event) {
		console.log("connected");
  	//	exampleSocket.send("Here's some text that the server is urgently awaiting!"); 
	};

  // allow use without new
 /* if (!(this instanceof WikiStream)) {
    return new WikiStream(options);
  }*/

 //Insert stream logic here
 //this.push('something');
 


  // init Transform
  Transform.call(this, options);


}
util.inherits(SampleStream, Transform);

//WikiStream.prototype._read = function(enc, cb) {};

exports = module.exports = SampleStream;
