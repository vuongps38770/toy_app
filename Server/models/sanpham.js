const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const sanpham= new Schema({
    tensanpham:{
        type: String,
        required:true,
        unique:true
    },
    listloaispconID:[{
        type: mongoose.Schema.Types.ObjectID,
        ref:"loaispcon",
    }],
    gia:{
        type:Number,
        required:true
    },
    mota:{
        type:String,
        required:true
    },
    thuonghieu:{
        type:Schema.Types.ObjectId,
        required:true,
        ref:'thuonghieu'
    },
    isActivate:{
        type:Number,
        required:true, 
    },
    isInMainScreen:{
        type:Number,
        required:true,
    }
},{
    collection:'sanpham',
    timestamps:true
})

module.exports= mongoose.model('sanpham',sanpham,'sanpham')
