const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const anhsp = new Schema({
    sanphamID:{
        type: mongoose.Schema.Types.ObjectID,
        ref:"sanpham",
        
    },
    url:{
        type:String,
        required:true,
    },
    isMain:{
        type:Number,
        required:true
    }
},{
    collection:'anhsp',
    timestamps:true
})
module.exports= mongoose.model('anhsp',anhsp,'anhsp')
