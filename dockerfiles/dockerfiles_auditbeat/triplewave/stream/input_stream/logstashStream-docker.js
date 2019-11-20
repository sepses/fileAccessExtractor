var stream = require('stream');
var util = require('util');

var Transform = stream.Transform || require('readable-stream').Transform;

function LogstashStream(options) {

writeStream.on('open', () => {
  let ws = websocket('http://logstash:3232/')

  ws.pipe(writeStream)

  ws.on('error', (err) => {
    if (err) throw err
  }).on('close', (err) => {
    if (err) throw err
    console.log(`Closing ws with: ${fs.statSync('big.out').size} bytes`)
  })

  writeStream.on('close', () => {
    console.log(`Closing file stream with: ${fs.statSync('big.out').size} bytes`)
  })
})

}
util.inherits(LogstashStream, Transform);

LogstashStream.prototype._read = function(enc, cb) {};

exports = module.exports = LogstashStream;
