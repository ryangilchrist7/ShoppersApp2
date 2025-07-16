package com.shoppersapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shoppersapp.R
import com.shoppersapp.model.Transaction

class TransactionAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

        fun updateData(newTransactions: List<Transaction>) {
            transactions = newTransactions
            notifyDataSetChanged()
        }

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.transactionAmountTextView)
        val type: TextView = view.findViewById(R.id.transactionTypeTextView)
        val timestamp: TextView = view.findViewById(R.id.transactionTimestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val tx = transactions[position]
        holder.amount.text = "£%.2f".format(tx.amount)
        holder.type.text = tx.type
        holder.timestamp.text = tx.timestamp.toString()
    }

    override fun getItemCount() = transactions.size
}