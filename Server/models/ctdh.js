const mongoose = require('mongoose')
const Schema = mongoose.Schema;

const ctdh=new Schema({
    sanphamID:{
        type:Schema.Types.ObjectId,
        required:true,
        ref:'sanpham'
    },
    donhangID:{
        type:Schema.Types.ObjectId,
        required:true,
        ref:'donhang'
    },
    dongia:{
        type:Number,
        required:true
    },
    soluong:{
        type:Number,
        required:true
    }
},{
    timestamps:true,
    collection:'ctdh'
})