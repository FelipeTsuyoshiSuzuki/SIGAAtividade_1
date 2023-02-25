package com.example.sigaatividade

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sigaatividade.databinding.FragmentDateBinding
import com.redmadrobot.inputmask.MaskedTextChangedListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateFragment : Fragment() {

    private lateinit var binding: FragmentDateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDateBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateMask = "[00]/[00]/[0000]"
        val listener = MaskedTextChangedListener(dateMask, binding.editTextBirthdate)
        val listener2 = MaskedTextChangedListener(dateMask, binding.editTextEnrollmentDate)
        binding.editTextBirthdate.addTextChangedListener(listener)
        binding.editTextEnrollmentDate.addTextChangedListener(listener2)

        binding.button.setOnClickListener {
            // Obter as datas de nascimento e ingresso na escola do estudante
            val dataNascimento = binding.editTextBirthdate.text.toString()
            val dataIngresso = binding.editTextEnrollmentDate.text.toString()

            // Converter as datas para o formato adequado
            val formato = SimpleDateFormat("dd/MM/yyyy")
            val dateNascimento = formato.parse(dataNascimento)
            val dateIngresso = transformStringToLocalDate(binding.editTextEnrollmentDate.text.toString())

            // Calcular a diferença de anos entre a data atual e a data de nascimento do estudante
            val hoje = LocalDate.now()
            val nascimento = dateNascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            // Calcular quantos anos o estudante ficou em cada unidade escolar
            val anosInfantil = calcularAnosNaUnidadeInfantil(nascimento, dateIngresso, hoje)
            val anosFundamental = calcularAnosNaUnidadeFundamental(nascimento, dateIngresso, hoje)
            val anosMedio = calcularAnosNaUnidadeMedio(nascimento, dateIngresso, hoje)
            val anosSuperior = calcularAnosNaUnidadeSuperior(nascimento, dateIngresso, hoje)
            val anosAlumni = calcularAnosNaUnidadeAlumni(nascimento, dateIngresso, hoje)

            // Exibir os resultados em um AlertDialog
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Resultados")
            builder.setMessage("O estudante ficou $anosInfantil anos na unidade infantil,\n $anosFundamental anos na unidade fundamental,\n $anosMedio anos na unidade médio,\n $anosSuperior anos na unidade superior e \n$anosAlumni anos na unidade alumni.")
            builder.setPositiveButton("OK", null)
            builder.show()
        }
    }

    fun transformStringToLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.parse(dateString, formatter)
    }

    fun calcularAnosNaUnidadeInfantil(dataNascimento: LocalDate, dataIngresso: LocalDate, dataAtual: LocalDate): Int {
        val dataMinima = dataNascimento.plusYears(6).plusMonths(6)
        val dataMaxima = dataNascimento.plusYears(11)
        val dataIngressoValida = if (dataIngresso.isBefore(dataMinima)) dataMinima else dataIngresso
        val dataSaidaValida = if (dataAtual.isAfter(dataMaxima)) dataMaxima else dataAtual
        return Period.between(dataIngressoValida, dataSaidaValida).years
    }

    fun calcularAnosNaUnidadeFundamental(dataNascimento: LocalDate, dataIngresso: LocalDate, dataAtual: LocalDate): Int {
        val dataMinima = dataNascimento.plusYears(11)
        val dataMaxima = dataNascimento.plusYears(15)
        val dataIngressoValida = if (dataIngresso.isBefore(dataMinima)) dataMinima else dataIngresso
        val dataSaidaValida = if (dataAtual.isAfter(dataMaxima)) dataMaxima else dataAtual
        return Period.between(dataIngressoValida, dataSaidaValida).years
    }

    fun calcularAnosNaUnidadeMedio(dataNascimento: LocalDate, dataIngresso: LocalDate, dataAtual: LocalDate): Int {
        val dataMinima = dataNascimento.plusYears(15)
        val dataMaxima = dataNascimento.plusYears(18)
        val dataIngressoValida = if (dataIngresso.isBefore(dataMinima)) dataMinima else dataIngresso
        val dataSaidaValida = if (dataAtual.isAfter(dataMaxima)) dataMaxima else dataAtual
        return Period.between(dataIngressoValida, dataSaidaValida).years
    }

    fun calcularAnosNaUnidadeSuperior(dataNascimento: LocalDate, dataIngresso: LocalDate, dataAtual: LocalDate): Int {
        val dataMinima = dataNascimento.plusYears(18)
        val dataMaxima = dataNascimento.plusYears(21)
        val dataIngressoValida = if (dataIngresso.isBefore(dataMinima)) dataMinima else dataIngresso
        val dataSaidaValida = if (dataAtual.isAfter(dataMaxima)) dataMaxima else dataAtual
        return Period.between(dataIngressoValida, dataSaidaValida).years
    }

    fun calcularAnosNaUnidadeAlumni(dataNascimento: LocalDate, dataIngresso: LocalDate, dataAtual: LocalDate): Int {
        val dataMinima = dataNascimento.plusYears(21)
        val dataIngressoValida = if (dataIngresso.isBefore(dataMinima)) dataMinima else dataIngresso
        return Period.between(dataIngressoValida, dataAtual).years
    }

}