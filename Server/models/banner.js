const mongoose = require("mongoose")
const Schema = mongoose.Schema

const Banner = new Schema({
    url1:{
        required:true,
        type:String,
    },
    url2:{
        required:true,
        type:String,
    },
    url3:{
        required:true,
        type:String,
    }

},{
    timestamps:true
})

module.exports=mongoose.model('banner',Banner,"banner")